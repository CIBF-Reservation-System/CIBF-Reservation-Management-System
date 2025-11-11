import { Stall } from "@/components/StallCard";

interface GoogleMapsVenueProps {
  stalls: Stall[];
  selectedStalls: Stall[];
  onStallSelect: (stall: Stall) => void;
}

export const GoogleMapsVenue = ({ stalls, selectedStalls, onStallSelect }: GoogleMapsVenueProps) => {
  // BMICH coordinates for embedded map
  const BMICH_LOCATION = "Bandaranaike Memorial International Conference Hall, Colombo";
  const embedUrl = `https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3960.798654785869!2d79.85919!3d6.9271!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3ae25963120b1509%3A0x2d1d6d8b8c8c8c8c!2sBandaranaike%20Memorial%20International%20Conference%20Hall!5e0!3m2!1sen!2slk!4v1234567890`;

  // Group stalls by area
  const stallsByArea = stalls.reduce((acc, stall) => {
    if (!acc[stall.area]) acc[stall.area] = [];
    acc[stall.area].push(stall);
    return acc;
  }, {} as Record<string, Stall[]>);

  return (
    <div className="w-full bg-card rounded-lg border p-6">
      <h3 className="text-lg font-semibold mb-4">BMICH Venue Location</h3>
      
      {/* Embedded Google Map */}
      <div className="w-full h-[500px] rounded-lg overflow-hidden mb-4">
        <iframe
          src={embedUrl}
          width="100%"
          height="100%"
          style={{ border: 0 }}
          allowFullScreen
          loading="lazy"
          referrerPolicy="no-referrer-when-downgrade"
          title="BMICH Location Map"
        />
      </div>

      {/* Stall Areas Info */}
      <div className="space-y-4">
        <h4 className="font-semibold text-base">Available Stall Areas</h4>
        <div className="grid sm:grid-cols-3 gap-4">
          {Object.entries(stallsByArea).map(([area, areaStalls]) => {
            const availableCount = areaStalls.filter(s => s.available).length;
            const totalCount = areaStalls.length;
            
            return (
              <div key={area} className="bg-muted/50 rounded-lg p-4">
                <h5 className="font-medium mb-2">{area}</h5>
                <p className="text-sm text-muted-foreground">
                  {availableCount} of {totalCount} stalls available
                </p>
                <div className="mt-2 space-y-1">
                  {areaStalls.slice(0, 3).map(stall => (
                    <div key={stall.id} className="text-xs">
                      <span className="text-foreground">
                        {stall.label} ({stall.size})
                      </span>
                    </div>
                  ))}
                  {areaStalls.length > 3 && (
                    <p className="text-xs text-muted-foreground">
                      +{areaStalls.length - 3} more
                    </p>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Info */}
      <div className="mt-4 p-4 bg-muted/30 rounded-lg">
        <p className="text-sm text-muted-foreground">
          📍 The book fair will be held at BMICH, Colombo. Use the filters and search below to find and select your preferred stalls.
        </p>
      </div>
    </div>
  );
};