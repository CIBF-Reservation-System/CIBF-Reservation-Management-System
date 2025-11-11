import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { useNavigate } from "react-router-dom";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox";
import { Stall } from "@/components/StallCard";
import { PaymentStep } from "@/components/PaymentStep";
import { useBookings } from "../hooks/useBookings";
import { reservationService } from "@/services/reservationService";


const reservationSchema = z.object({
  businessName: z
    .string()
    .trim()
    .min(2, { message: "Business name must be at least 2 characters" })
    .max(100, { message: "Business name must be less than 100 characters" }),
  email: z
    .string()
    .trim()
    .email({ message: "Please enter a valid email address" })
    .max(255, { message: "Email must be less than 255 characters" }),
  phone: z
    .string()
    .trim()
    .regex(/^[0-9+\-\s()]+$/, { message: "Please enter a valid phone number" })
    .min(10, { message: "Phone number must be at least 10 digits" })
    .max(20, { message: "Phone number must be less than 20 characters" }),
  acceptTerms: z.boolean().refine((val) => val === true, {
    message: "You must accept the terms and conditions",
  }),
});

type ReservationFormData = z.infer<typeof reservationSchema>;

interface ReservationModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  selectedStalls: Stall[];
  totalPrice: number;
  onConfirm: (data: ReservationFormData & { referenceNumber: string }) => void;
}

export const ReservationModal = ({
  open,
  onOpenChange,
  selectedStalls,
  totalPrice,
  onConfirm,
}: ReservationModalProps) => {
  const navigate = useNavigate();
  const { addBooking } = useBookings();
  const [step, setStep] = useState<"details" | "payment">("details");
  const [formData, setFormData] = useState<ReservationFormData | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    setValue,
    watch,
    reset,
  } = useForm<ReservationFormData>({
    resolver: zodResolver(reservationSchema),
    defaultValues: {
      acceptTerms: false,
    },
  });

  const acceptTerms = watch("acceptTerms");

  const generateReferenceNumber = () => {
    const prefix = "CBF";
    const timestamp = Date.now().toString(36).toUpperCase();
    const random = Math.random().toString(36).substring(2, 6).toUpperCase();
    return `${prefix}-${timestamp}-${random}`;
  };

  const onSubmit = async (data: ReservationFormData) => {
    setFormData(data);
    setStep("payment");
  };

  // const handlePaymentComplete = async () => {
  //   if (!formData) return;

  //   const refNumber = generateReferenceNumber();
  //   const bookingId = `booking-${Date.now()}`;

  //   // Create booking object
  //   const newBooking = {
  //     id: bookingId,
  //     ref: refNumber,
  //     stalls: selectedStalls.map(s => s.label),
  //     date: "March 15-17, 2026",
  //     status: "Confirmed" as const,
  //     businessName: formData.businessName,
  //     email: formData.email,
  //     phone: formData.phone,
  //     totalPrice,
  //     bookingDate: new Date().toISOString(),
  //     paymentStatus: "Paid" as const,
  //   };

  //   // Save to localStorage
  //   addBooking(newBooking);

  //   // Call parent callback
  //   onConfirm({ ...formData, referenceNumber: refNumber });
    
  //   // Close modal and reset
  //   onOpenChange(false);
  //   reset();
  //   setStep("details");
  //   setFormData(null);

  //   // Navigate to success page
  //   setTimeout(() => {
  //     navigate("/confirmation", {
  //       state: {
  //         referenceNumber: refNumber,
  //         stallLabels: selectedStalls.map(s => s.label),
  //         businessName: formData.businessName,
  //         email: formData.email,
  //         totalPrice,
  //       },
  //     });
  //   }, 100);
  // };
  const handlePaymentComplete = async () => {
  if (!formData) return;

  console.log(formData)

  const refNumber = generateReferenceNumber();
  const bookingId = `booking-${Date.now()}`;

  const userId = localStorage.getItem("userId");
  
  if (!userId) {
    console.error("User ID not found in localStorage");
    return;
  }

  // Build the reservation payload (array of reservations)
  const reservationRequests = selectedStalls.map((stall) => ({
    userId: userId,
    stallId: stall.id,
    businessName: formData.businessName,
    email: formData.email,
    phoneNumber: formData.phone,
  }));

  try {
    // 🔹 Send reservation request to backend
    await reservationService.makeReservation(reservationRequests);


    console.log(reservationRequests)
    // 🔹 Create a local booking record (for UI/localStorage)
    // const newBooking = {
    //   id: bookingId,
    //   ref: refNumber,
    //   stalls: selectedStalls.map((s) => s.label),
    //   date: "March 15-17, 2026",
    //   status: "Confirmed" as const,
    //   businessName: formData.businessName,
    //   email: formData.email,
    //   phone: formData.phone,
    //   totalPrice,
    //   bookingDate: new Date().toISOString(),
    //   paymentStatus: "Paid" as const,
    // };

   // addBooking(newBooking);
   // onConfirm({ ...formData, referenceNumber: refNumber });

    // Reset modal and navigate
    onOpenChange(false);
    reset();
    setStep("details");
    setFormData(null);

    setTimeout(() => {
      navigate("/confirmation", {
        state: {
          referenceNumber: refNumber,
          stallLabels: selectedStalls.map((s) => s.label),
          businessName: formData.businessName,
          email: formData.email,
          totalPrice,
        },
      });
    }, 100);
  } catch (error) {
    console.error("Reservation failed:", error);
    alert("Failed to make reservation. Please try again.");
  }
};

  const handleClose = () => {
    if (!isSubmitting) {
      reset();
      setStep("details");
      setFormData(null);
      onOpenChange(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={handleClose}>
      <DialogContent className="sm:max-w-lg max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>
            {step === "details" ? "Confirm Your Reservation" : "Complete Payment"}
          </DialogTitle>
          <DialogDescription>
            {step === "details" 
              ? "Please provide your business details to continue."
              : "Complete your payment to confirm your booking."}
          </DialogDescription>
        </DialogHeader>

        {step === "payment" ? (
          <PaymentStep
            totalPrice={totalPrice}
            onPaymentComplete={handlePaymentComplete}
            onBack={() => setStep("details")}
          />
        ) : (
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          {/* Selected Stalls Summary */}
          <div className="rounded-lg border bg-muted/30 p-4 space-y-2">
            <h4 className="font-medium text-sm">Selected Stalls</h4>
            {selectedStalls.map((stall) => (
              <div
                key={stall.id}
                className="flex justify-between text-sm items-center"
              >
                <span>
                  {stall.label} <span className="text-muted-foreground">({stall.size})</span>
                </span>
                <span className="font-medium">
                  LKR {stall.price.toLocaleString()}
                </span>
              </div>
            ))}
            <div className="border-t pt-2 flex justify-between font-semibold">
              <span>Total</span>
              <span>LKR {totalPrice.toLocaleString()}</span>
            </div>
          </div>

          {/* Business Information */}
          <div className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="businessName">
                Business Name <span className="text-destructive">*</span>
              </Label>
              <Input
                id="businessName"
                placeholder="Enter your business name"
                {...register("businessName")}
                aria-invalid={errors.businessName ? "true" : "false"}
              />
              {errors.businessName && (
                <p className="text-sm text-destructive">
                  {errors.businessName.message}
                </p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="email">
                Email Address <span className="text-destructive">*</span>
              </Label>
              <Input
                id="email"
                type="email"
                placeholder="your.email@example.com"
                {...register("email")}
                aria-invalid={errors.email ? "true" : "false"}
              />
              {errors.email && (
                <p className="text-sm text-destructive">
                  {errors.email.message}
                </p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="phone">
                Phone Number <span className="text-destructive">*</span>
              </Label>
              <Input
                id="phone"
                type="tel"
                placeholder="+94 XX XXX XXXX"
                {...register("phone")}
                aria-invalid={errors.phone ? "true" : "false"}
              />
              {errors.phone && (
                <p className="text-sm text-destructive">
                  {errors.phone.message}
                </p>
              )}
            </div>
          </div>

          {/* Terms and Conditions */}
          <div className="space-y-2">
            <div className="flex items-start space-x-2">
              <Checkbox
                id="acceptTerms"
                checked={acceptTerms}
                onCheckedChange={(checked) =>
                  setValue("acceptTerms", checked === true)
                }
              />
              <Label
                htmlFor="acceptTerms"
                className="text-sm font-normal leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
              >
                I accept the{" "}
                <a
                  href="/terms"
                  className="text-primary hover:underline"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  terms and conditions
                </a>{" "}
                and understand the cancellation policy
              </Label>
            </div>
            {errors.acceptTerms && (
              <p className="text-sm text-destructive">
                {errors.acceptTerms.message}
              </p>
            )}
          </div>

            <DialogFooter className="gap-2 sm:gap-0">
              <Button
                type="button"
                variant="outline"
                onClick={handleClose}
                disabled={isSubmitting}
              >
                Cancel
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Processing..." : "Continue to Payment"}
              </Button>
            </DialogFooter>
          </form>
        )}
      </DialogContent>
    </Dialog>
  );
};