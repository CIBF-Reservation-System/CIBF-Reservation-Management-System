
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import { useToast } from "@/hooks/use-toast";
import { Check } from "lucide-react";

const AVAILABLE_GENRES = [
  "Fiction",
  "Non-Fiction",
  "Mystery",
  "Romance",
  "Science Fiction",
  "Fantasy",
  "Thriller",
  "Horror",
  "Biography",
  "History",
  "Self-Help",
  "Business",
  "Children's Books",
  "Young Adult",
  "Poetry",
  "Comics & Graphic Novels",
  "Cookbooks",
  "Travel",
  "Art & Photography",
  "Religion & Spirituality",
];

const Genres = () => {
  const navigate = useNavigate();
  const { toast } = useToast();
  const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
  const [description, setDescription] = useState("");
  const [instagram, setInstagram] = useState("");
  const [facebook, setFacebook] = useState("");
  const [website, setWebsite] = useState("");

  const toggleGenre = (genre: string) => {
    setSelectedGenres((prev) =>
      prev.includes(genre) ? prev.filter((g) => g !== genre) : [...prev, genre]
    );
  };

  const handleSave = () => {
    // Save preferences (would connect to backend)
    console.log({
      genres: selectedGenres,
      description,
      socialHandles: { instagram, facebook, website },
    });

    toast({
      title: "Preferences Saved",
      description: "Your booth preferences have been updated successfully.",
    });

    // Navigate to bookings page
    navigate("/bookings");
  };

  return (
    <div className="min-h-screen flex flex-col bg-background">
      <Header />
      
      <main className="flex-1 container mx-auto px-4 py-8 max-w-4xl">
        <div className="space-y-8">
          {/* Header */}
          <div className="space-y-2">
            <h1 className="text-3xl font-bold">Booth Preferences</h1>
            <p className="text-muted-foreground">
              Help visitors discover your booth by selecting genres and adding details about your business.
            </p>
          </div>

          {/* Genre Selection */}
          <div className="space-y-4">
            <div>
              <Label className="text-lg font-semibold">Select Your Genres</Label>
              <p className="text-sm text-muted-foreground mt-1">
                Choose all genres that apply to your books and products
              </p>
            </div>
            
            <div className="flex flex-wrap gap-2">
              {AVAILABLE_GENRES.map((genre) => {
                const isSelected = selectedGenres.includes(genre);
                return (
                  <Badge
                    key={genre}
                    variant={isSelected ? "default" : "outline"}
                    className="cursor-pointer px-4 py-2 text-sm transition-all hover:scale-105"
                    onClick={() => toggleGenre(genre)}
                  >
                    {isSelected && <Check className="w-3 h-3 mr-1" />}
                    {genre}
                  </Badge>
                );
              })}
            </div>
            
            {selectedGenres.length > 0 && (
              <p className="text-sm text-muted-foreground">
                {selectedGenres.length} genre{selectedGenres.length !== 1 ? "s" : ""} selected
              </p>
            )}
          </div>

          {/* Business Description */}
          <div className="space-y-2">
            <Label htmlFor="description" className="text-lg font-semibold">
              Business Description <span className="text-muted-foreground font-normal">(Optional)</span>
            </Label>
            <p className="text-sm text-muted-foreground">
              Tell visitors about your business, books, or what they can expect at your booth
            </p>
            <Textarea
              id="description"
              placeholder="e.g., Independent publisher specializing in local authors and illustrated children's books. We also offer book signing events and reading sessions."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="min-h-[120px] resize-none"
              maxLength={500}
            />
            <p className="text-xs text-muted-foreground text-right">
              {description.length}/500 characters
            </p>
          </div>

          {/* Social Handles */}
          <div className="space-y-4">
            <div>
              <Label className="text-lg font-semibold">
                Social Media & Website <span className="text-muted-foreground font-normal">(Optional)</span>
              </Label>
              <p className="text-sm text-muted-foreground mt-1">
                Share your online presence with visitors
              </p>
            </div>

            <div className="grid gap-4 sm:grid-cols-2">
              <div className="space-y-2">
                <Label htmlFor="instagram">Instagram</Label>
                <Input
                  id="instagram"
                  placeholder="@yourusername"
                  value={instagram}
                  onChange={(e) => setInstagram(e.target.value)}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="facebook">Facebook</Label>
                <Input
                  id="facebook"
                  placeholder="facebook.com/yourpage"
                  value={facebook}
                  onChange={(e) => setFacebook(e.target.value)}
                />
              </div>

              <div className="space-y-2 sm:col-span-2">
                <Label htmlFor="website">Website</Label>
                <Input
                  id="website"
                  type="url"
                  placeholder="https://yourwebsite.com"
                  value={website}
                  onChange={(e) => setWebsite(e.target.value)}
                />
              </div>
            </div>
          </div>

          {/* Actions */}
          <div className="flex gap-3 pt-4">
            <Button
              size="lg"
              onClick={handleSave}
              className="flex-1 sm:flex-none"
            >
              Save Preferences
            </Button>
            <Button
              size="lg"
              variant="outline"
              onClick={() => navigate("/bookings")}
              className="flex-1 sm:flex-none"
            >
              Skip for Now
            </Button>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default Genres;
