
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent } from "@/components/ui/card";
import { toast } from "@/hooks/use-toast";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Search } from "lucide-react";
// Local Stall types and a minimal StallCard component to avoid a missing module error
type StallSize = "Small" | "Medium" | "Large";
interface Stall {
  id: string;
  label: string;
  size: StallSize;
  price: number;
  available: boolean;
  area: string;
}

function StallCard({
  stall,
  isSelected,
  onSelect,
}: {
  stall: Stall;
  isSelected: boolean;
  onSelect: (stall: Stall) => void;
}) {
  return (
    <div
      onClick={() => onSelect(stall)}
      className={`border rounded p-3 cursor-pointer ${isSelected ? "border-primary bg-primary/10" : "border-muted"}`}
      role="button"
      aria-pressed={isSelected}
    >
      <div className="flex items-center justify-between">
        <div>
          <div className="font-semibold">{stall.label}</div>
          <div className="text-xs text-muted-foreground">{stall.size} — {stall.area}</div>
        </div>
        <div className="text-sm">LKR {stall.price.toLocaleString()}</div>
      </div>
      {!stall.available && <div className="text-xs text-destructive mt-2">Unavailable</div>}
    </div>
  );
}

// Minimal inline ReservationModal fallback to avoid missing module error
export function ReservationModal({
  open,
  onOpenChange,
  selectedStalls,
  totalPrice,
  onConfirm,
}: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  selectedStalls: Stall[];
  totalPrice: number;
  onConfirm: (data: any) => void;
}) {
  if (!open) return null;

  return (
    <div role="dialog" aria-modal="true" className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded shadow-lg w-full max-w-md">
        <h2 className="text-lg font-semibold mb-2">Confirm Reservation</h2>
        <p className="text-sm text-muted-foreground mb-4">
          {selectedStalls.length} stalls — Total: LKR {totalPrice.toLocaleString()}
        </p>

        <div className="space-y-2 mb-4">
          {selectedStalls.map((s) => (
            <div key={s.id} className="flex justify-between text-sm">
              <span>{s.label} ({s.size})</span>
              <span>LKR {s.price.toLocaleString()}</span>
            </div>
          ))}
        </div>

        <div className="flex gap-2 justify-end">
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            Cancel
          </Button>
          <Button
            onClick={() => {
              const referenceNumber = `REF-${Math.random().toString(36).slice(2, 9).toUpperCase()}`;
              onConfirm({ referenceNumber });
              onOpenChange(false);
            }}
          >
            Confirm
          </Button>
        </div>
      </div>
    </div>
  );
}

const mockStalls: Stall[] = [
  { id: "1", label: "A1", size: "Small", price: 15000, available: true, area: "Hall A" },
  { id: "2", label: "A2", size: "Medium", price: 25000, available: true, area: "Hall A" },
  { id: "3", label: "A3", size: "Large", price: 40000, available: false, area: "Hall A" },
  { id: "4", label: "A4", size: "Small", price: 15000, available: true, area: "Hall A" },
  { id: "5", label: "B1", size: "Small", price: 15000, available: true, area: "Hall B" },
  { id: "6", label: "B2", size: "Medium", price: 25000, available: true, area: "Hall B" },
  { id: "7", label: "B3", size: "Large", price: 40000, available: true, area: "Hall B" },
  { id: "8", label: "B4", size: "Medium", price: 25000, available: false, area: "Hall B" },
  { id: "9", label: "C1", size: "Small", price: 15000, available: true, area: "Outdoor" },
  { id: "10", label: "C2", size: "Medium", price: 25000, available: false, area: "Outdoor" },
  { id: "11", label: "C3", size: "Large", price: 40000, available: true, area: "Outdoor" },
  { id: "12", label: "C4", size: "Small", price: 15000, available: true, area: "Outdoor" },
];

const Reserve = () => {
  const navigate = useNavigate();
  const [selectedSize, setSelectedSize] = useState<StallSize | "All">("All");
  const [selectedArea, setSelectedArea] = useState<string>("All");
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedStalls, setSelectedStalls] = useState<Stall[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const areas = ["All", "Hall A", "Hall B", "Outdoor"];

  const filteredStalls = mockStalls.filter((stall) => {
    const matchesSize = selectedSize === "All" || stall.size === selectedSize;
    const matchesArea = selectedArea === "All" || stall.area === selectedArea;
    const matchesSearch = stall.label.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesSize && matchesArea && matchesSearch;
  });

  const handleStallSelect = (stall: Stall) => {
    if (selectedStalls.find((s) => s.id === stall.id)) {
      setSelectedStalls(selectedStalls.filter((s) => s.id !== stall.id));
    } else if (selectedStalls.length < 3) {
      setSelectedStalls([...selectedStalls, stall]);
    } else {
      toast({
        title: "Limit reached",
        description: "You can only reserve up to 3 stalls",
        variant: "destructive",
      });
    }
  };

  const totalPrice = selectedStalls.reduce((sum, stall) => sum + stall.price, 0);

  const handleConfirmReservation = (data: any) => {
    toast({
      title: "Reservation successful!",
      description: `Reference: ${data.referenceNumber}. Check your email for QR pass.`,
    });
    
    // Clear selections and redirect to bookings
    setSelectedStalls([]);
    setTimeout(() => {
      navigate("/bookings");
    }, 500);
  };

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Header />
      <main className="flex-1 container mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold mb-2">Reserve Your Stall</h1>
        <p className="text-muted-foreground mb-8">Select up to 3 stalls for your booth</p>

        {/* Filters */}
        <div className="mb-8 space-y-4">
          {/* Size Filters */}
          <div>
            <p className="text-sm font-medium mb-2">Stall Size</p>
            <div className="flex flex-wrap gap-2">
              {(["All", "Small", "Medium", "Large"] as const).map((size) => (
                <Button
                  key={size}
                  variant={selectedSize === size ? "default" : "outline"}
                  onClick={() => setSelectedSize(size)}
                >
                  {size}
                </Button>
              ))}
            </div>
          </div>

          {/* Area Filters */}
          <div>
            <p className="text-sm font-medium mb-2">Area / Zone</p>
            <div className="flex flex-wrap gap-2">
              {areas.map((area) => (
                <Button
                  key={area}
                  variant={selectedArea === area ? "default" : "outline"}
                  onClick={() => setSelectedArea(area)}
                >
                  {area}
                </Button>
              ))}
            </div>
          </div>

          {/* Search */}
          <div className="relative max-w-md">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Search by stall label..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10"
            />
          </div>
        </div>

        <div className="grid lg:grid-cols-3 gap-8">
          {/* Stall Grid */}
          <div className="lg:col-span-2">
            {filteredStalls.length === 0 ? (
              <Card className="p-12 text-center">
                <p className="text-muted-foreground">
                  No stalls found matching your filters. Try adjusting your search.
                </p>
              </Card>
            ) : (
              <div className="grid sm:grid-cols-2 md:grid-cols-3 gap-4">
                {filteredStalls.map((stall) => {
                  const isSelected = !!selectedStalls.find((s) => s.id === stall.id);
                  return (
                    <StallCard
                      key={stall.id}
                      stall={stall}
                      isSelected={isSelected}
                      onSelect={handleStallSelect}
                    />
                  );
                })}
              </div>
            )}
          </div>

          {/* Selection Summary */}
          <div className="lg:col-span-1">
            <Card className="sticky top-4">
              <CardContent className="p-6 space-y-4">
                <h3 className="font-semibold text-lg">Selected Stalls</h3>
                
                {selectedStalls.length === 0 ? (
                  <p className="text-muted-foreground text-sm">No stalls selected</p>
                ) : (
                  <div className="space-y-2">
                    {selectedStalls.map((stall) => (
                      <div key={stall.id} className="flex justify-between text-sm">
                        <span>{stall.label} ({stall.size})</span>
                        <span>LKR {stall.price.toLocaleString()}</span>
                      </div>
                    ))}
                  </div>
                )}

                <div className="border-t pt-4">
                  <div className="flex justify-between font-semibold mb-4">
                    <span>Total</span>
                    <span>LKR {totalPrice.toLocaleString()}</span>
                  </div>
                  
                  <Button 
                    className="w-full" 
                    disabled={selectedStalls.length === 0}
                    onClick={() => setIsModalOpen(true)}
                  >
                    Confirm Reservation
                  </Button>
                  
                  <p className="text-xs text-muted-foreground mt-2 text-center">
                    {selectedStalls.length}/3 stalls selected
                  </p>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </main>
      <Footer />

      <ReservationModal
        open={isModalOpen}
        onOpenChange={setIsModalOpen}
        selectedStalls={selectedStalls}
        totalPrice={totalPrice}
        onConfirm={handleConfirmReservation}
      />
    </div>
  );
};

export default Reserve;
