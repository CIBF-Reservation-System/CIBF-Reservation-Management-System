import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { toast } from "@/hooks/use-toast";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Calendar, MapPin } from "lucide-react";
import { reservationService } from "@/services/reservationService";
import { stallService } from "@/services/stallService";

interface Reservation {
  reservationId: string;
  userId: string;
  stallId: string;
  businessName: string;
  email: string;
  phoneNumber: string;
  reservationDate: string;
  status: string;
  createdAt: string;
  updatedAt: string;
  message: string | null;
  error: string | null;
}

interface Stall {
  stallId: string;
  label: string;
  stallSize: string;
  price: number;
  availability: number;
  area: string;
}

interface ReservationWithStall extends Reservation {
  stallDetails?: Stall;
}

const Bookings = () => {
  const [reservations, setReservations] = useState<ReservationWithStall[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchReservations = async () => {
      try {
        const userId = localStorage.getItem("userId");
        if (!userId) {
          toast({ title: "User not logged in", variant: "destructive" });
          return;
        }

        // 1. Fetch reservations
        const data = await reservationService.getUserReservations(userId);

        // 2. Fetch stall details for each reservation in parallel
        const reservationsWithStalls: ReservationWithStall[] = await Promise.all(
          data.map(async (res: Reservation) => {
            try {
              const stallDetails = await stallService.getStallById(res.stallId);
              return { ...res, stallDetails };
            } catch (err) {
              console.error("Error fetching stall details for", res.stallId);
              return res; // fallback to reservation without stall details
            }
          })
        );

        setReservations(reservationsWithStalls);
      } catch (err: any) {
        toast({ title: "Error fetching reservations", description: err?.message, variant: "destructive" });
      } finally {
        setLoading(false);
      }
    };

    fetchReservations();
  }, []);

  if (loading) {
    return <p className="text-center py-8">Loading reservations...</p>;
  }

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Header />
      <main className="flex-1 container mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold mb-2">My Reservations</h1>
        {reservations.length === 0 ? (
          <p className="text-muted-foreground mt-4">No reservations found.</p>
        ) : (
          <div className="space-y-6">
            {reservations.map((res) => (
              <Card key={res.reservationId}>
                <CardHeader className="bg-muted/30 flex justify-between items-center">
                  <div>
                    <CardTitle className="text-xl">{res.businessName}</CardTitle>
                    <div className="flex gap-4 text-sm text-muted-foreground mt-1">
                      <div className="flex items-center gap-1">
                        <Calendar className="h-4 w-4" />
                        {new Date(res.reservationDate).toLocaleString()}
                        
                      </div>
                      {res.stallDetails ? (
                        <div className="flex items-center gap-1">
                          <MapPin className="h-4 w-4" />
                          {res.stallDetails.label} 
                        </div>
                      ) : (
                        <div className="flex items-center gap-1">
                          <MapPin className="h-4 w-4" />
                          {res.stallId}
                        </div>
                      )}
                    </div>
                  </div>
                  <Badge variant={res.status === "PENDING" ? "default" : "destructive"}>
                    {res.status}
                  </Badge>
                </CardHeader>
                <CardContent className="p-4">
                  <p>Email: {res.email}</p>
                  <p>Phone: {res.phoneNumber}</p>
                  <p>Area: {res.stallDetails.area}</p>
                  <p>Size: {res.stallDetails.stallSize}</p>
                  <p>Price: LKR {res.stallDetails.price.toLocaleString()}</p>

                  {/* ({res.stallDetails.area}, {res.stallDetails.stallSize}, LKR {res.stallDetails.price.toLocaleString()}) */}
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
