import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { toast } from "@/hooks/use-toast";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { QRPassCard } from "@/components/QRPassCard";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Calendar, MapPin, Mail, Info } from "lucide-react";
import { useState } from "react";
import { useBookings, Booking } from "@/hooks/useBookings";

const Bookings = () => {
  const { bookings, cancelBooking: cancelBookingInStorage } = useBookings();
  const [cancellingBooking, setCancellingBooking] = useState<Booking | null>(null);
  const [detailBooking, setDetailBooking] = useState<Booking | null>(null);

  const activeBookings = bookings.filter((b) => b.status === "Confirmed");
  const pastBookings = bookings.filter((b) => b.status === "Cancelled");

  const handleDownloadQR = () => {
    toast({
      title: "QR Pass Downloaded",
      description: "Your entry pass has been saved to your device",
    });
  };

  const handleCancelConfirm = () => {
    if (cancellingBooking) {
      cancelBookingInStorage(cancellingBooking.id);
      toast({
        title: "Booking Cancelled",
        description: `Reservation ${cancellingBooking.ref} has been cancelled`,
        variant: "destructive",
      });
      setCancellingBooking(null);
    }
  };

  const handleResendEmail = (booking: Booking) => {
    toast({
      title: "Email Sent",
      description: `Confirmation email resent to ${booking.email}`,
    });
  };

  const renderBookingCard = (booking: Booking) => (
    <Card key={booking.id} className="overflow-hidden">
      <CardHeader className="bg-muted/30">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <CardTitle className="text-xl">Reservation {booking.ref}</CardTitle>
            <div className="flex items-center gap-4 mt-2 text-sm text-muted-foreground">
              <div className="flex items-center gap-1">
                <Calendar className="h-4 w-4" />
                {booking.date}
              </div>
              <div className="flex items-center gap-1">
                <MapPin className="h-4 w-4" />
                {booking.stalls.join(", ")}
              </div>
            </div>
          </div>
          <Badge variant={booking.status === "Confirmed" ? "default" : "destructive"}>
            {booking.status}
          </Badge>
        </div>
      </CardHeader>
      <CardContent className="p-6">
        <div className="grid md:grid-cols-2 gap-6">
          <div>
            <QRPassCard
              referenceNumber={booking.ref}
              stallLabels={booking.stalls}
              eventDate={booking.date}
              onDownload={handleDownloadQR}
            />
          </div>

          <div className="space-y-4">
            <div className="p-4 bg-muted/50 rounded-lg">
              <h4 className="text-sm font-semibold mb-3">Stalls Reserved</h4>
              <div className="flex flex-wrap gap-2">
                {booking.stalls.map((stall) => (
                  <Badge key={stall} variant="secondary">
                    {stall}
                  </Badge>
                ))}
              </div>
            </div>

            <div className="flex gap-2">
              <Button
                variant="outline"
                className="flex-1"
                onClick={() => setDetailBooking(booking)}
              >
                <Info className="h-4 w-4 mr-2" />
                Details
              </Button>
              {booking.status === "Confirmed" && (
                <Button
                  variant="outline"
                  className="flex-1"
                  onClick={() => handleResendEmail(booking)}
                >
                  <Mail className="h-4 w-4 mr-2" />
                  Resend Email
                </Button>
              )}
            </div>

            {booking.status === "Confirmed" && (
              <Button
                variant="destructive"
                className="w-full"
                onClick={() => setCancellingBooking(booking)}
              >
                Cancel Booking
              </Button>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  );

  const renderEmptyState = (type: "active" | "past") => (
    <Card>
      <CardContent className="p-12 text-center">
        <div className="mx-auto w-24 h-24 bg-muted rounded-full flex items-center justify-center mb-4">
          <Calendar className="h-12 w-12 text-muted-foreground" />
        </div>
        <h3 className="text-xl font-semibold mb-2">
          {type === "active" ? "No Active Bookings" : "No Past Bookings"}
        </h3>
        <p className="text-muted-foreground mb-4">
          {type === "active"
            ? "You don't have any active reservations at the moment."
            : "You don't have any past reservations."}
        </p>
        {type === "active" && (
          <Button onClick={() => (window.location.href = "/reserve")}>
            Reserve a Stall
          </Button>
        )}
      </CardContent>
    </Card>
  );

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Header />
      <main className="flex-1 container mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold mb-2">My Bookings</h1>
        <p className="text-muted-foreground mb-8">
          View and manage your stall reservations
        </p>

        <Tabs defaultValue="active" className="w-full">
          <TabsList className="grid w-full max-w-md grid-cols-2">
            <TabsTrigger value="active">
              Active ({activeBookings.length})
            </TabsTrigger>
            <TabsTrigger value="past">Past ({pastBookings.length})</TabsTrigger>
          </TabsList>

          <TabsContent value="active" className="mt-6">
            {activeBookings.length === 0 ? (
              renderEmptyState("active")
            ) : (
              <div className="space-y-6">
                {activeBookings.map((booking) => renderBookingCard(booking))}
              </div>
            )}
          </TabsContent>

          <TabsContent value="past" className="mt-6">
            {pastBookings.length === 0 ? (
              renderEmptyState("past")
            ) : (
              <div className="space-y-6">
                {pastBookings.map((booking) => renderBookingCard(booking))}
              </div>
            )}
          </TabsContent>
        </Tabs>
      </main>

      {/* Cancel Booking Dialog */}
      <AlertDialog
        open={!!cancellingBooking}
        onOpenChange={(open) => !open && setCancellingBooking(null)}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Cancel Booking?</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to cancel reservation{" "}
              <strong>{cancellingBooking?.ref}</strong>? This action cannot be
              undone and may result in cancellation fees.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Keep Booking</AlertDialogCancel>
            <AlertDialogAction
              onClick={handleCancelConfirm}
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            >
              Cancel Booking
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      {/* Booking Details Dialog */}
      <Dialog open={!!detailBooking} onOpenChange={(open) => !open && setDetailBooking(null)}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>Booking Details</DialogTitle>
            <DialogDescription>
              Reference: {detailBooking?.ref}
            </DialogDescription>
          </DialogHeader>
          {detailBooking && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-sm font-semibold text-muted-foreground">
                    Business Name
                  </label>
                  <p className="text-base">{detailBooking.businessName}</p>
                </div>
                <div>
                  <label className="text-sm font-semibold text-muted-foreground">
                    Status
                  </label>
                  <div className="mt-1">
                    <Badge
                      variant={
                        detailBooking.status === "Confirmed"
                          ? "default"
                          : "destructive"
                      }
                    >
                      {detailBooking.status}
                    </Badge>
                  </div>
                </div>
                <div>
                  <label className="text-sm font-semibold text-muted-foreground">
                    Email
                  </label>
                  <p className="text-base">{detailBooking.email}</p>
                </div>
                <div>
                  <label className="text-sm font-semibold text-muted-foreground">
                    Booking Date
                  </label>
                  <p className="text-base">
                    {new Date(detailBooking.bookingDate).toLocaleDateString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm font-semibold text-muted-foreground">
                    Event Date
                  </label>
                  <p className="text-base">{detailBooking.date}</p>
                </div>
                <div>
                  <label className="text-sm font-semibold text-muted-foreground">
                    Total Price
                  </label>
                  <p className="text-base font-semibold">
                    LKR {detailBooking.totalPrice.toLocaleString()}
                  </p>
                </div>
              </div>
              <div>
                <label className="text-sm font-semibold text-muted-foreground">
                  Reserved Stalls
                </label>
                <div className="flex flex-wrap gap-2 mt-2">
                  {detailBooking.stalls.map((stall) => (
                    <Badge key={stall} variant="secondary">
                      {stall}
                    </Badge>
                  ))}
                </div>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>

      <Footer />
    </div>
  );
};

export default Bookings;