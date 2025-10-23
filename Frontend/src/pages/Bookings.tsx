import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { toast } from "@/hooks/use-toast";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Download, Calendar, MapPin } from "lucide-react";

interface Booking {
  id: string;
  ref: string;
  stalls: string[];
  date: string;
  status: "Confirmed" | "Cancelled";
  qrCode: string;
}

const mockBookings: Booking[] = [
  {
    id: "1",
    ref: "CBF2026-001",
    stalls: ["A1", "B2"],
    date: "March 15-17, 2026",
    status: "Confirmed",
    qrCode: "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Crect fill='%23fff' width='100' height='100'/%3E%3Cpath fill='%23000' d='M10 10h15v15H10zm20 0h5v5h-5zm10 0h5v5h-5zm10 0h15v15H65zM10 30h5v5h-5zm20 0h5v5h-5zm10 0h10v5H40zm20 0h5v5h-5zM10 40h5v5h-5zm10 0h5v5h-5zm10 0h5v5h-5zm10 0h5v5h-5zm10 0h5v5h-5zm10 0h5v5h-5zM10 50h15v15H10zm20 0h5v5h-5zm10 0h5v5h-5zm10 0h15v15H65z'/%3E%3C/svg%3E",
  },
];

const Bookings = () => {
  const handleDownloadQR = (ref: string) => {
    toast({
      title: "QR Code Downloaded",
      description: `Your QR pass for ${ref} has been saved`,
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
                    <div className="space-y-4">
                      <h3 className="font-semibold">Your QR Pass</h3>
                      <div className="bg-card-elevated p-4 rounded-lg border">
                        <img
                          src={booking.qrCode}
                          alt="QR Code"
                          className="w-48 h-48 mx-auto"
                        />
                        <p className="text-xs text-center text-muted-foreground mt-2">
                          Show this QR at the entrance
                        </p>
                      </div>
                    </div>

                    <div className="space-y-4">
                      <h3 className="font-semibold">Actions</h3>
                      <div className="space-y-2">
                        <Button
                          variant="outline"
                          className="w-full justify-start"
                          onClick={() => handleDownloadQR(booking.ref)}
                        >
                          <Download className="h-4 w-4 mr-2" />
                          Download QR Pass
                        </Button>
                        
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

                      <div className="p-4 bg-muted/50 rounded-lg mt-4">
                        <h4 className="text-sm font-semibold mb-2">Stalls Reserved</h4>
                        <div className="flex flex-wrap gap-2">
                          {booking.stalls.map((stall) => (
                            <Badge key={stall} variant="secondary">
                              {stall}
                            </Badge>
                          ))}
                        </div>
                      </div>
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
