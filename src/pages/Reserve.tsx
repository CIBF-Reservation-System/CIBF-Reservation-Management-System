import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import { toast } from "@/hooks/use-toast";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Search } from "lucide-react";

type StallSize = "Small" | "Medium" | "Large";

interface Stall {
  id: string;
  label: string;
  size: StallSize;
  price: number;
  available: boolean;
}

const mockStalls: Stall[] = [
  { id: "1", label: "A1", size: "Small", price: 15000, available: true },
  { id: "2", label: "A2", size: "Medium", price: 25000, available: true },
  { id: "3", label: "A3", size: "Large", price: 40000, available: false },
  { id: "4", label: "B1", size: "Small", price: 15000, available: true },
  { id: "5", label: "B2", size: "Medium", price: 25000, available: true },
  { id: "6", label: "B3", size: "Large", price: 40000, available: true },
  { id: "7", label: "C1", size: "Small", price: 15000, available: true },
  { id: "8", label: "C2", size: "Medium", price: 25000, available: false },
  { id: "9", label: "C3", size: "Large", price: 40000, available: true },
];

const Reserve = () => {
  const [selectedSize, setSelectedSize] = useState<StallSize | "All">("All");
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedStalls, setSelectedStalls] = useState<Stall[]>([]);

  const filteredStalls = mockStalls.filter((stall) => {
    const matchesSize = selectedSize === "All" || stall.size === selectedSize;
    const matchesSearch = stall.label.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesSize && matchesSearch && stall.available;
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

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <Header />
      <main className="flex-1 container mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold mb-2">Reserve Your Stall</h1>
        <p className="text-muted-foreground mb-8">Select up to 3 stalls for your booth</p>

        {/* Filters */}
        <div className="mb-8 space-y-4">
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
            <div className="grid sm:grid-cols-2 md:grid-cols-3 gap-4">
              {filteredStalls.map((stall) => {
                const isSelected = selectedStalls.find((s) => s.id === stall.id);
                return (
                  <Card
                    key={stall.id}
                    className={`cursor-pointer transition-all hover:scale-105 hover:shadow-elegant ${
                      isSelected ? "ring-2 ring-primary" : ""
                    }`}
                    onClick={() => handleStallSelect(stall)}
                  >
                    <CardContent className="p-6 text-center space-y-3">
                      <div className="text-3xl font-bold text-primary">{stall.label}</div>
                      <Badge variant="secondary">{stall.size}</Badge>
                      <div className="text-xl font-semibold">
                        LKR {stall.price.toLocaleString()}
                      </div>
                    </CardContent>
                  </Card>
                );
              })}
            </div>
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
                    onClick={() => toast({
                      title: "Coming soon",
                      description: "Reservation confirmation will be added next",
                    })}
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
    </div>
  );
};

export default Reserve;
