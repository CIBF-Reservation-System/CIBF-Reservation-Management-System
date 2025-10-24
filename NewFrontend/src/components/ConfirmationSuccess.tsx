import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { QRPassCard } from "@/components/QRPassCard";
import { CheckCircle2, ArrowRight } from "lucide-react";
import confetti from "canvas-confetti";
import { toast } from "@/hooks/use-toast";

interface ConfirmationSuccessProps {
  referenceNumber: string;
  stallLabels: string[];
  businessName: string;
  email: string;
  totalPrice: number;
}

export const ConfirmationSuccess = ({
  referenceNumber,
  stallLabels,
  businessName,
  email,
  totalPrice,
}: ConfirmationSuccessProps) => {
  const navigate = useNavigate();
  const eventDate = "March 15-17, 2026";

  useEffect(() => {
    // Trigger confetti animation
    const duration = 3000;
    const end = Date.now() + duration;

    const colors = ["#0E8A8A", "#FF8A4B", "#7BAF6F"];

    (function frame() {
      confetti({
        particleCount: 3,
        angle: 60,
        spread: 55,
        origin: { x: 0 },
        colors: colors,
      });
      confetti({
        particleCount: 3,
        angle: 120,
        spread: 55,
        origin: { x: 1 },
        colors: colors,
      });

      if (Date.now() < end) {
        requestAnimationFrame(frame);
      }
    })();
  }, []);

  const handleDownloadQR = () => {
    toast({
      title: "QR Pass Downloaded",
      description: "Your entry pass has been saved to your device",
    });
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <div className="max-w-2xl w-full space-y-6">
        <Card className="border-primary/20">
          <CardHeader className="text-center pb-4">
            <div className="flex justify-center mb-4">
              <div className="rounded-full bg-primary/10 p-3">
                <CheckCircle2 className="h-12 w-12 text-primary" />
              </div>
            </div>
            <CardTitle className="text-3xl">Reservation Confirmed!</CardTitle>
            <p className="text-muted-foreground mt-2">
              Thank you, {businessName}! Your stall reservation has been confirmed.
            </p>
          </CardHeader>

          <CardContent className="space-y-6">
            <div className="bg-muted/30 rounded-lg p-6 space-y-3">
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <p className="text-muted-foreground">Reference Number</p>
                  <p className="font-semibold">{referenceNumber}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Event Date</p>
                  <p className="font-semibold">{eventDate}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Stalls Reserved</p>
                  <p className="font-semibold">{stallLabels.join(", ")}</p>
                </div>
                <div>
                  <p className="text-muted-foreground">Total Amount</p>
                  <p className="font-semibold">Rs. {totalPrice.toLocaleString()}</p>
                </div>
              </div>
            </div>

            <QRPassCard
              referenceNumber={referenceNumber}
              stallLabels={stallLabels}
              eventDate={eventDate}
              onDownload={handleDownloadQR}
            />

            <div className="bg-accent/50 rounded-lg p-4 text-sm">
              <p className="font-medium mb-1">📧 Confirmation Email Sent</p>
              <p className="text-muted-foreground">
                A confirmation email with your QR pass has been sent to <strong>{email}</strong>
              </p>
            </div>

            <div className="flex flex-col sm:flex-row gap-3">
              <Button
                variant="outline"
                className="flex-1"
                onClick={() => navigate("/bookings")}
              >
                View My Bookings
              </Button>
              <Button
                className="flex-1"
                onClick={() => navigate("/reserve")}
              >
                Reserve More Stalls
                <ArrowRight className="h-4 w-4 ml-2" />
              </Button>
            </div>
          </CardContent>
        </Card>

        <p className="text-center text-sm text-muted-foreground">
          Need help? Contact us at support@colombobookfair.lk
        </p>
      </div>
    </div>
  );
};
