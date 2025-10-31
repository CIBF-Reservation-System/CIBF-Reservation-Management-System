
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import heroImage from "@/assets/hero-bookfair.jpg";
import { Calendar, QrCode, Tag, ArrowRight } from "lucide-react";
import { useNavigate } from "react-router-dom";

const Index = () => {
  const navigate = useNavigate();

  const scrollToSection = (id: string) => {
    const element = document.getElementById(id);
    element?.scrollIntoView({ behavior: "smooth" });
  };

  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      
      <main className="flex-1">
        {/* Hero Section */}
        <section className="relative gradient-subtle min-h-[600px] flex items-center">
          <div className="container mx-auto px-4 py-20">
            <div className="grid md:grid-cols-2 gap-12 items-center">
              <div className="space-y-6 animate-fade-in">
                <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-foreground leading-tight">
                  Reserve your stall at Colombo Bookfair 2026
                </h1>
                <p className="text-lg text-muted-foreground max-w-xl">
                  Simple bookings, QR entry, and personalized genre badges.
                </p>
                <div className="flex flex-col sm:flex-row gap-4">
                  <Button size="lg" className="text-base px-8" onClick={() => navigate("/reserve")}>
                    Reserve a Stall
                    <ArrowRight className="ml-2 w-4 h-4" />
                  </Button>
                  <Button size="lg" variant="outline" className="text-base px-8" onClick={() => scrollToSection("how-it-works")}>
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

        {/* How It Works Section */}
        <section id="how-it-works" className="py-20 bg-background">
          <div className="container mx-auto px-4">
            <div className="text-center mb-12 animate-fade-in">
              <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
                How It Works
              </h2>
              <p className="text-muted-foreground max-w-2xl mx-auto">
                Get your stall up and running in three simple steps
              </p>
            </div>

            <div className="grid md:grid-cols-3 gap-8 max-w-5xl mx-auto">
              <Card className="relative animate-fade-in hover-scale">
                <CardHeader>
                  <div className="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                    <Calendar className="w-6 h-6 text-primary" />
                  </div>
                  <CardTitle className="text-xl">1. Choose Your Stall</CardTitle>
                </CardHeader>
                <CardContent>
                  <CardDescription className="text-base">
                    Browse available stalls, select your preferred location, and pick a date that works for you.
                  </CardDescription>
                </CardContent>
              </Card>

              <Card className="relative animate-fade-in hover-scale" style={{ animationDelay: "0.1s" }}>
                <CardHeader>
                  <div className="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                    <QrCode className="w-6 h-6 text-primary" />
                  </div>
                  <CardTitle className="text-xl">2. Receive Your QR Pass</CardTitle>
                </CardHeader>
                <CardContent>
                  <CardDescription className="text-base">
                    Get instant confirmation via email with your unique QR code for seamless entry to the event.
                  </CardDescription>
                </CardContent>
              </Card>

              <Card className="relative animate-fade-in hover-scale" style={{ animationDelay: "0.2s" }}>
                <CardHeader>
                  <div className="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                    <Tag className="w-6 h-6 text-primary" />
                  </div>
                  <CardTitle className="text-xl">3. Set Your Genre Tags</CardTitle>
                </CardHeader>
                <CardContent>
                  <CardDescription className="text-base">
                    Personalize your stall badge with genre tags to help visitors find exactly what they're looking for.
                  </CardDescription>
                </CardContent>
              </Card>
            </div>
          </div>
        </section>

        {/* Featured FAQs Section */}
        <section className="py-20 gradient-subtle">
          <div className="container mx-auto px-4">
            <div className="text-center mb-12 animate-fade-in">
              <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
                Frequently Asked Questions
              </h2>
              <p className="text-muted-foreground max-w-2xl mx-auto">
                Everything you need to know about reserving your stall
              </p>
            </div>

            <div className="max-w-3xl mx-auto animate-fade-in">
              <Accordion type="single" collapsible className="w-full">
                <AccordionItem value="item-1">
                  <AccordionTrigger className="text-left">
                    How much does it cost to reserve a stall?
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground">
                    Stall prices vary by location and size. Standard stalls start at LKR 5,000, corner stalls at LKR 7,500, 
                    and premium locations at LKR 10,000. All prices are per day.
                  </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-2">
                  <AccordionTrigger className="text-left">
                    Can I cancel or modify my booking?
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground">
                    Yes, you can cancel or modify your booking up to 7 days before the event date. 
                    Cancellations made within 7 days are subject to a 25% cancellation fee.
                  </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-3">
                  <AccordionTrigger className="text-left">
                    What's included with my stall reservation?
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground">
                    Each stall includes a table, two chairs, basic lighting, and Wi-Fi access. 
                    You'll also receive a personalized genre badge and a QR code for easy event entry.
                  </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-4">
                  <AccordionTrigger className="text-left">
                    How do I access the event with my QR code?
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground">
                    Simply present your QR code (available in your email or booking dashboard) at the vendor entrance. 
                    Our staff will scan it, and you'll be directed to your stall location.
                  </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-5">
                  <AccordionTrigger className="text-left">
                    Can I reserve multiple stalls?
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground">
                    Absolutely! You can select only three stalls during the booking process. 
                    
                  </AccordionContent>
                </AccordionItem>
              </Accordion>
            </div>
          </div>
        </section>
      </main>

      <Footer />
    </div>
  );
};

export default Index;
