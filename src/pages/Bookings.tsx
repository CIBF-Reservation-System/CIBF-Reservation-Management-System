import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { toast } from "@/hooks/use-toast";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { QRPassCard } from "@/components/QRPassCard";
import { Calendar, MapPin } from "lucide-react";

interface Booking {
  id: string;
  ref: string;
  stalls: string[];
  date: string;
  status: "Confirmed" | "Cancelled";
}

const mockBookings: Booking[] = [
  {
    id: "1",
    ref: "CBF-1734567890-A1B2",
    stalls: ["A1", "B2"],
    date: "March 15-17, 2026",
    status: "Confirmed",
  },
];

const Bookings = () => {
  const handleDownloadQR = () => {
    toast({
      title: "QR Pass Downloaded",
      description: "Your entry pass has been saved to your device",
    });
  };

  const handleCancel = (ref: string) => {
    toast({
      title: "Booking Cancelled",
      description: `Reservation ${ref} has been cancelled`,
      variant: "destructive",
    });
  };

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Header />
      <main className="flex-1 container mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold mb-2">My Bookings</h1>
        <p className="text-muted-foreground mb-8">View and manage your stall reservations</p>

        {mockBookings.length === 0 ? (
          <Card>
            <CardContent className="p-12 text-center">
              <p className="text-muted-foreground mb-4">You have no active bookings.</p>
              <Button>Reserve a Stall</Button>
            </CardContent>
          </Card>
        ) : (
          <div className="space-y-6">
            {mockBookings.map((booking) => (
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

                      {booking.status === "Confirmed" && (
                        <Button
                          variant="destructive"
                          className="w-full"
                          onClick={() => handleCancel(booking.ref)}
                        >
                          Cancel Booking
                        </Button>
                      )}
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </main>
      <Footer />
    </div>
  );
};

export default Bookings;
