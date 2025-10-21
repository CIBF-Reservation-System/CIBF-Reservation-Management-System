import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Check } from "lucide-react";

export type StallSize = "Small" | "Medium" | "Large";

export interface Stall {
  id: string;
  label: string;
  size: StallSize;
  price: number;
  available: boolean;
  area: string;
}

interface StallCardProps {
  stall: Stall;
  isSelected: boolean;
  onSelect: (stall: Stall) => void;
}

export const StallCard = ({ stall, isSelected, onSelect }: StallCardProps) => {
  return (
    <Card
      className={`cursor-pointer transition-all duration-200 hover:scale-105 hover:shadow-elegant relative group ${
        isSelected ? "ring-2 ring-primary shadow-elegant" : ""
      } ${!stall.available ? "opacity-50 cursor-not-allowed" : ""}`}
      onClick={() => stall.available && onSelect(stall)}
    >
      <CardContent className="p-6 text-center space-y-3">
        {/* Selection indicator */}
        {isSelected && (
          <div className="absolute top-2 right-2 bg-primary rounded-full p-1">
            <Check className="h-4 w-4 text-primary-foreground" />
          </div>
        )}

        {/* Stall label */}
        <div className="text-3xl font-bold text-primary">{stall.label}</div>

        {/* Size badge */}
        <Badge variant="secondary">{stall.size}</Badge>

        {/* Area badge */}
        <Badge variant="outline" className="text-xs">
          {stall.area}
        </Badge>

        {/* Price */}
        <div className="text-xl font-semibold">
          LKR {stall.price.toLocaleString()}
        </div>

        {/* Availability status */}
        {!stall.available && (
          <div className="text-sm text-muted-foreground font-medium">
            Reserved
          </div>
        )}

        {/* Hover tooltip */}
        <div className="absolute inset-0 bg-primary/5 opacity-0 group-hover:opacity-100 transition-opacity duration-200 rounded-md pointer-events-none" />
      </CardContent>
    </Card>
  );
};