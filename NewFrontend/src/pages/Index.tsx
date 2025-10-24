import { Button } from "@/components/ui/button";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import heroImage from "@/assets/hero-bookfair.jpg";

const Index = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      
      <main className="flex-1">
        {/* Hero Section */}
        <section className="relative gradient-subtle min-h-[600px] flex items-center">
          <div className="container mx-auto px-4 py-20">
            <div className="grid md:grid-cols-2 gap-12 items-center">
              <div className="space-y-6 animate-fade-in">
                <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-slate leading-tight">
                  Reserve your stall at Colombo Bookfair 2026
                </h1>
                <p className="text-lg text-muted-foreground max-w-xl">
                  Simple bookings, QR entry, and personalized genre badges. Join Sri Lanka's premier literary event.
                </p>
                <div className="flex flex-col sm:flex-row gap-4">
                  <Button size="lg" className="text-base px-8">
                    Reserve a Stall
                  </Button>
                  <Button size="lg" variant="outline" className="text-base px-8">
                    How It Works
                  </Button>
                </div>
              </div>
              
              <div className="relative animate-scale-in">
                <img 
                  src={heroImage} 
                  alt="Colombo Bookfair venue with diverse visitors browsing book stalls" 
                  className="rounded-lg shadow-lg w-full h-auto"
                />
              </div>
            </div>
          </div>
        </section>
      </main>

      <Footer />
    </div>
  );
};

export default Index;
