import { Stall } from "@/components/StallCard";

interface VenueMapProps {
  stalls: Stall[];
  selectedStalls: Stall[];
  onStallSelect: (stall: Stall) => void;
  highlightedArea?: string;
}

export const VenueMap = ({ stalls, selectedStalls, onStallSelect, highlightedArea }: VenueMapProps) => {
  const getStallColor = (stall: Stall) => {
    if (!stall.available) return "hsl(var(--muted))";
    if (selectedStalls.find(s => s.id === stall.id)) return "hsl(var(--primary))";
    return "hsl(var(--secondary))";
  };

  const getStallOpacity = (stall: Stall) => {
    if (highlightedArea && highlightedArea !== "All" && stall.area !== highlightedArea) {
      return 0.3;
    }
    return 1;
  };

  const getStallBorderColor = (stall: Stall) => {
    if (selectedStalls.find(s => s.id === stall.id)) return "hsl(var(--primary))";
    return "hsl(var(--border))";
  };

  return (
    <div className="w-full bg-card rounded-lg border p-6">
      <h3 className="text-lg font-semibold mb-4">Venue Map</h3>
      
      <svg viewBox="0 0 600 400" className="w-full h-auto" style={{ cursor: 'default' }}>
        <style>{`
          .stall-available { cursor: pointer; }
          .stall-available:hover rect { filter: brightness(1.2); }
          .stall-unavailable { cursor: not-allowed; }
        `}</style>
        {/* Hall A */}
        <g>
          <rect x="20" y="20" width="260" height="160" fill="hsl(var(--muted))" opacity="0.1" stroke="hsl(var(--border))" strokeWidth="2" rx="4" />
          <text x="150" y="15" textAnchor="middle" className="text-xs font-semibold fill-foreground">Hall A</text>
          
          {/* Hall A Stalls */}
          {stalls.filter(s => s.area === "Hall A").map((stall, idx) => {
            const positions = [
              { x: 40, y: 40 },   // A1
              { x: 160, y: 40 },  // A2
              { x: 40, y: 110 },  // A3
              { x: 160, y: 110 }  // A4
            ];
            const pos = positions[idx] || positions[0];
            
            return (
              <g 
                key={stall.id} 
                className={stall.available ? "stall-available" : "stall-unavailable"}
                onClick={() => stall.available && onStallSelect(stall)}
                opacity={getStallOpacity(stall)}
                style={{ pointerEvents: 'all' }}
              >
                <rect 
                  x={pos.x} 
                  y={pos.y} 
                  width={stall.size === "Large" ? 90 : stall.size === "Medium" ? 70 : 50} 
                  height={stall.size === "Large" ? 50 : stall.size === "Medium" ? 45 : 40}
                  fill={getStallColor(stall)}
                  stroke={getStallBorderColor(stall)}
                  strokeWidth="2"
                  rx="2"
                />
                <text 
                  x={pos.x + (stall.size === "Large" ? 45 : stall.size === "Medium" ? 35 : 25)} 
                  y={pos.y + (stall.size === "Large" ? 30 : stall.size === "Medium" ? 27 : 25)}
                  textAnchor="middle" 
                  className="text-sm font-bold fill-foreground"
                >
                  {stall.label}
                </text>
              </g>
            );
          })}
        </g>

        {/* Hall B */}
        <g>
          <rect x="320" y="20" width="260" height="160" fill="hsl(var(--muted))" opacity="0.1" stroke="hsl(var(--border))" strokeWidth="2" rx="4" />
          <text x="450" y="15" textAnchor="middle" className="text-xs font-semibold fill-foreground">Hall B</text>
          
          {/* Hall B Stalls */}
          {stalls.filter(s => s.area === "Hall B").map((stall, idx) => {
            const positions = [
              { x: 340, y: 40 },   // B1
              { x: 460, y: 40 },   // B2
              { x: 340, y: 110 },  // B3
              { x: 460, y: 110 }   // B4
            ];
            const pos = positions[idx] || positions[0];
            
            return (
              <g 
                key={stall.id}
                className={stall.available ? "stall-available" : "stall-unavailable"}
                onClick={() => stall.available && onStallSelect(stall)}
                opacity={getStallOpacity(stall)}
                style={{ pointerEvents: 'all' }}
              >
                <rect 
                  x={pos.x} 
                  y={pos.y} 
                  width={stall.size === "Large" ? 90 : stall.size === "Medium" ? 70 : 50} 
                  height={stall.size === "Large" ? 50 : stall.size === "Medium" ? 45 : 40}
                  fill={getStallColor(stall)}
                  stroke={getStallBorderColor(stall)}
                  strokeWidth="2"
                  rx="2"
                />
                <text 
                  x={pos.x + (stall.size === "Large" ? 45 : stall.size === "Medium" ? 35 : 25)} 
                  y={pos.y + (stall.size === "Large" ? 30 : stall.size === "Medium" ? 27 : 25)}
                  textAnchor="middle" 
                  className="text-sm font-bold fill-foreground"
                >
                  {stall.label}
                </text>
              </g>
            );
          })}
        </g>

        {/* Outdoor Area */}
        <g>
          <rect x="20" y="220" width="560" height="160" fill="hsl(var(--muted))" opacity="0.1" stroke="hsl(var(--border))" strokeWidth="2" rx="4" strokeDasharray="5,5" />
          <text x="300" y="215" textAnchor="middle" className="text-xs font-semibold fill-foreground">Outdoor Area</text>
          
          {/* Outdoor Stalls */}
          {stalls.filter(s => s.area === "Outdoor").map((stall, idx) => {
            const positions = [
              { x: 60, y: 240 },   // C1
              { x: 220, y: 240 },  // C2
              { x: 380, y: 240 },  // C3
              { x: 490, y: 240 }   // C4
            ];
            const pos = positions[idx] || positions[0];
            
            return (
              <g 
                key={stall.id}
                className={stall.available ? "stall-available" : "stall-unavailable"}
                onClick={() => stall.available && onStallSelect(stall)}
                opacity={getStallOpacity(stall)}
                style={{ pointerEvents: 'all' }}
              >
                <rect 
                  x={pos.x} 
                  y={pos.y} 
                  width={stall.size === "Large" ? 90 : stall.size === "Medium" ? 70 : 50} 
                  height={stall.size === "Large" ? 110 : stall.size === "Medium" ? 100 : 90}
                  fill={getStallColor(stall)}
                  stroke={getStallBorderColor(stall)}
                  strokeWidth="2"
                  rx="2"
                />
                <text 
                  x={pos.x + (stall.size === "Large" ? 45 : stall.size === "Medium" ? 35 : 25)} 
                  y={pos.y + (stall.size === "Large" ? 60 : stall.size === "Medium" ? 55 : 50)}
                  textAnchor="middle" 
                  className="text-sm font-bold fill-foreground"
                >
                  {stall.label}
                </text>
              </g>
            );
          })}
        </g>

        {/* Legend */}
        <g transform="translate(0, 360)">
          <rect x="20" y="15" width="20" height="15" fill="hsl(var(--secondary))" stroke="hsl(var(--border))" strokeWidth="1" rx="2" />
          <text x="45" y="27" className="text-xs fill-muted-foreground">Available</text>
          
          <rect x="130" y="15" width="20" height="15" fill="hsl(var(--primary))" stroke="hsl(var(--primary))" strokeWidth="1" rx="2" />
          <text x="155" y="27" className="text-xs fill-muted-foreground">Selected</text>
          
          <rect x="240" y="15" width="20" height="15" fill="hsl(var(--muted))" stroke="hsl(var(--border))" strokeWidth="1" rx="2" />
          <text x="265" y="27" className="text-xs fill-muted-foreground">Reserved</text>
        </g>
      </svg>
    </div>
  );
};
