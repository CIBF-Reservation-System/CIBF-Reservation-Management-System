
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Badge } from "@/components/ui/badge";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import heroImage from "@/assets/hero-bookfair.jpg";
import { Calendar, QrCode, Tag, ArrowRight, BookOpen, Users, MapPin, Sparkles, CheckCircle2 } from "lucide-react";
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
        <section className="relative bg-background min-h-[90vh] flex items-center overflow-hidden">
          <div className="container mx-auto px-4 py-16 sm:py-20 lg:py-28 relative z-10">
            <div className="grid lg:grid-cols-2 gap-12 lg:gap-20 items-center">
              <div className="space-y-8 animate-fade-in">
               
                <h1 className="text-5xl sm:text-6xl lg:text-7xl font-bold text-foreground leading-tight tracking-tight">
                  Reserve Your Stall at{" "}
                  
                    Colombo Bookfair 2026
                  
                </h1>
                
                <p className="text-lg sm:text-xl lg:text-2xl text-muted-foreground max-w-2xl leading-relaxed">
                  Simple bookings, QR entry, and personalized genre badges. Join Sri Lanka's premier literary event.
                </p>
                
                <div className="flex flex-col sm:flex-row gap-4 pt-6">
                  <Button 
                    size="lg" 
                    className="text-base sm:text-lg px-8 sm:px-10 h-14 hover-lift shadow-lg hover:shadow-xl transition-all duration-300 group" 
                    onClick={() => navigate("/reserve")}
                  >
                    Reserve a Stall
                    <ArrowRight className="ml-2 w-5 h-5 group-hover:translate-x-1 transition-transform" />
                  </Button>
                  <Button 
                    size="lg" 
                    variant="outline" 
                    className="text-base sm:text-lg px-8 sm:px-10 h-14 hover:border-primary hover:text-primary transition-all duration-300" 
                    onClick={() => scrollToSection("how-it-works")}
                  >
                    How It Works
                  </Button>
                </div>
                
                <div className="flex flex-wrap items-center gap-6 lg:gap-8 pt-6">
                  <div className="flex items-center gap-2.5 group">
                    <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center group-hover:bg-primary/20 transition-colors">
                      <CheckCircle2 className="w-4 h-4 text-primary" />
                    </div>
                    <span className="text-sm font-medium text-muted-foreground">Instant Confirmation</span>
                  </div>
                  <div className="flex items-center gap-2.5 group">
                    <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center group-hover:bg-primary/20 transition-colors">
                      <CheckCircle2 className="w-4 h-4 text-primary" />
                    </div>
                    <span className="text-sm font-medium text-muted-foreground">Secure Payment</span>
                  </div>
                  <div className="flex items-center gap-2.5 group">
                    <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center group-hover:bg-primary/20 transition-colors">
                      <CheckCircle2 className="w-4 h-4 text-primary" />
                    </div>
                    <span className="text-sm font-medium text-muted-foreground">Free Cancellation</span>
                  </div>
                </div>
              </div>
              
              <div className="relative order-first lg:order-last group">
                <div className="absolute -inset-4 bg-gradient-to-r from-primary/20 to-secondary/20 rounded-3xl blur-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
                <div className="relative overflow-hidden rounded-3xl shadow-2xl border border-border/50 transform group-hover:scale-[1.02] transition-all duration-500">
                  <img 
                    src={heroImage} 
                    alt="Colombo Bookfair venue with diverse visitors browsing book stalls" 
                    className="w-full h-auto object-cover"
                  />
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Stats Section */}
        <section className="py-20 bg-card border-y border-border/50 relative overflow-hidden">
          <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-secondary/5"></div>
          <div className="container mx-auto px-4 relative">
            <div className="grid grid-cols-2 lg:grid-cols-4 gap-8 lg:gap-12 max-w-6xl mx-auto">
              <div className="text-center animate-fade-in group hover:scale-105 transition-transform duration-300">
                <div className="text-5xl lg:text-6xl font-bold bg-gradient-to-br from-primary to-primary/70 bg-clip-text text-transparent mb-3">200+</div>
                <div className="text-sm lg:text-base font-medium text-muted-foreground">Vendors</div>
                <div className="w-16 h-1 bg-gradient-to-r from-primary/0 via-primary to-primary/0 mx-auto mt-3 rounded-full"></div>
              </div>
              <div className="text-center animate-fade-in group hover:scale-105 transition-transform duration-300" style={{ animationDelay: "0.1s" }}>
                <div className="text-5xl lg:text-6xl font-bold bg-gradient-to-br from-secondary to-secondary/70 bg-clip-text text-transparent mb-3">50K+</div>
                <div className="text-sm lg:text-base font-medium text-muted-foreground">Visitors Expected</div>
                <div className="w-16 h-1 bg-gradient-to-r from-secondary/0 via-secondary to-secondary/0 mx-auto mt-3 rounded-full"></div>
              </div>
              <div className="text-center animate-fade-in group hover:scale-105 transition-transform duration-300" style={{ animationDelay: "0.2s" }}>
                <div className="text-5xl lg:text-6xl font-bold bg-gradient-to-br from-olive to-olive/70 bg-clip-text text-transparent mb-3">7</div>
                <div className="text-sm lg:text-base font-medium text-muted-foreground">Days of Books</div>
                <div className="w-16 h-1 bg-gradient-to-r from-olive/0 via-olive to-olive/0 mx-auto mt-3 rounded-full"></div>
              </div>
              <div className="text-center animate-fade-in group hover:scale-105 transition-transform duration-300" style={{ animationDelay: "0.3s" }}>
                <div className="text-5xl lg:text-6xl font-bold bg-gradient-to-br from-primary to-secondary bg-clip-text text-transparent mb-3">20+</div>
                <div className="text-sm lg:text-base font-medium text-muted-foreground">Literary Events</div>
                <div className="w-16 h-1 bg-gradient-to-r from-primary/0 via-secondary to-primary/0 mx-auto mt-3 rounded-full"></div>
              </div>
            </div>
          </div>
        </section>

        {/* How It Works Section */}
        <section id="how-it-works" className="py-24 lg:py-32 bg-background relative">
          <div className="container mx-auto px-4">
            <div className="text-center mb-20 animate-fade-in">
              <Badge variant="outline" className="mb-6 px-4 py-2 text-sm font-semibold">Simple Process</Badge>
              <h2 className="text-4xl md:text-5xl lg:text-6xl font-bold text-foreground mb-6 tracking-tight">
                How It Works
              </h2>
              <p className="text-lg md:text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed">
                Get your stall up and running in three simple steps
              </p>
            </div>

            <div className="grid md:grid-cols-3 gap-8 lg:gap-10 max-w-7xl mx-auto">
              <Card className="relative animate-fade-in hover-lift border-2 hover:border-primary/50 transition-all duration-500 group bg-card/50 backdrop-blur-sm overflow-hidden cursor-pointer hover:shadow-2xl">
                <div className="absolute inset-0 bg-gradient-to-br from-primary/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
                <div className="absolute -top-4 -right-4 w-14 h-14 rounded-full bg-gradient-to-br from-primary to-primary/80 text-primary-foreground flex items-center justify-center font-bold text-xl shadow-lg group-hover:scale-125 group-hover:rotate-12 transition-all duration-500">
                  1
                </div>
                <CardHeader className="relative">
                  <div className="w-20 h-20 rounded-2xl bg-gradient-to-br from-primary/20 to-primary/5 flex items-center justify-center mb-6 group-hover:scale-110 group-hover:rotate-3 transition-all duration-300">
                    <Calendar className="w-10 h-10 text-primary" />
                  </div>
                  <CardTitle className="text-2xl lg:text-3xl mb-3">Choose Your Stall</CardTitle>
                </CardHeader>
                <CardContent className="relative">
                  <CardDescription className="text-base lg:text-lg leading-relaxed">
                    Browse available stalls, select your preferred location, and pick a date that works for you. View pricing and amenities for each spot.
                  </CardDescription>
                </CardContent>
              </Card>

              <Card className="relative animate-fade-in hover-lift border-2 hover:border-secondary/50 transition-all duration-500 group bg-card/50 backdrop-blur-sm overflow-hidden cursor-pointer hover:shadow-2xl" style={{ animationDelay: "0.1s" }}>
                <div className="absolute inset-0 bg-gradient-to-br from-secondary/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
                <div className="absolute -top-4 -right-4 w-14 h-14 rounded-full bg-gradient-to-br from-secondary to-secondary/80 text-secondary-foreground flex items-center justify-center font-bold text-xl shadow-lg group-hover:scale-125 group-hover:rotate-12 transition-all duration-500">
                  2
                </div>
                <CardHeader className="relative">
                  <div className="w-20 h-20 rounded-2xl bg-gradient-to-br from-secondary/20 to-secondary/5 flex items-center justify-center mb-6 group-hover:scale-110 group-hover:rotate-3 transition-all duration-300">
                    <QrCode className="w-10 h-10 text-secondary" />
                  </div>
                  <CardTitle className="text-2xl lg:text-3xl mb-3">Receive Your QR Pass</CardTitle>
                </CardHeader>
                <CardContent className="relative">
                  <CardDescription className="text-base lg:text-lg leading-relaxed">
                    Get instant confirmation via email with your unique QR code for seamless entry. Save it to your phone or print it out.
                  </CardDescription>
                </CardContent>
              </Card>

              <Card className="relative animate-fade-in hover-lift border-2 hover:border-olive/50 transition-all duration-500 group bg-card/50 backdrop-blur-sm overflow-hidden cursor-pointer hover:shadow-2xl" style={{ animationDelay: "0.2s" }}>
                <div className="absolute inset-0 bg-gradient-to-br from-olive/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
                <div className="absolute -top-4 -right-4 w-14 h-14 rounded-full bg-gradient-to-br from-olive to-olive/80 text-primary-foreground flex items-center justify-center font-bold text-xl shadow-lg group-hover:scale-125 group-hover:rotate-12 transition-all duration-500">
                  3
                </div>
                <CardHeader className="relative">
                  <div className="w-20 h-20 rounded-2xl bg-gradient-to-br from-olive/20 to-olive/5 flex items-center justify-center mb-6 group-hover:scale-110 group-hover:rotate-3 transition-all duration-300">
                    <Tag className="w-10 h-10 text-olive" />
                  </div>
                  <CardTitle className="text-2xl lg:text-3xl mb-3">Set Your Genre Tags</CardTitle>
                </CardHeader>
                <CardContent className="relative">
                  <CardDescription className="text-base lg:text-lg leading-relaxed">
                    Personalize your stall badge with genre tags to help visitors find exactly what they're looking for. Stand out from the crowd.
                  </CardDescription>
                </CardContent>
              </Card>
            </div>
          </div>
        </section>

        {/* Benefits Section */}
        <section className="py-24 lg:py-32 gradient-subtle relative overflow-hidden">
          <div className="absolute inset-0 opacity-30">
            <div className="absolute top-0 left-1/4 w-96 h-96 bg-primary/10 rounded-full filter blur-3xl"></div>
            <div className="absolute bottom-0 right-1/4 w-96 h-96 bg-secondary/10 rounded-full filter blur-3xl"></div>
          </div>
          
          <div className="container mx-auto px-4 relative">
            <div className="text-center mb-20 animate-fade-in">
              <Badge variant="outline" className="mb-6 px-4 py-2 text-sm font-semibold">Why Choose Us</Badge>
              <h2 className="text-4xl md:text-5xl lg:text-6xl font-bold text-foreground mb-6 tracking-tight">
                Everything You Need to Succeed
              </h2>
              <p className="text-lg md:text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed">
                We provide all the tools and support to make your bookfair experience seamless
              </p>
            </div>

            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8 lg:gap-10 max-w-7xl mx-auto">
              <div className="flex gap-5 animate-fade-in group p-6 rounded-2xl hover:bg-card/80 backdrop-blur-sm transition-all duration-500 hover:shadow-xl cursor-pointer hover:-translate-y-1">
                <div className="flex-shrink-0">
                  <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-primary/20 to-primary/5 flex items-center justify-center group-hover:scale-110 group-hover:rotate-6 transition-all duration-300">
                    <MapPin className="w-7 h-7 text-primary" />
                  </div>
                </div>
                <div>
                  <h3 className="font-bold text-xl mb-3 text-foreground">Prime Locations</h3>
                  <p className="text-muted-foreground leading-relaxed">Choose from corner stalls, high-traffic areas, and premium spots with maximum visibility.</p>
                </div>
              </div>

              <div className="flex gap-5 animate-fade-in group p-6 rounded-2xl hover:bg-card/80 backdrop-blur-sm transition-all duration-500 hover:shadow-xl cursor-pointer hover:-translate-y-1" style={{ animationDelay: "0.1s" }}>
                <div className="flex-shrink-0">
                  <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-secondary/20 to-secondary/5 flex items-center justify-center group-hover:scale-110 group-hover:rotate-6 transition-all duration-300">
                    <Users className="w-7 h-7 text-secondary" />
                  </div>
                </div>
                <div>
                  <h3 className="font-bold text-xl mb-3 text-foreground">Connect with Readers</h3>
                  <p className="text-muted-foreground leading-relaxed">Meet thousands of book lovers actively searching for their next great read.</p>
                </div>
              </div>

              <div className="flex gap-5 animate-fade-in group p-6 rounded-2xl hover:bg-card/80 backdrop-blur-sm transition-all duration-500 hover:shadow-xl cursor-pointer hover:-translate-y-1" style={{ animationDelay: "0.2s" }}>
                <div className="flex-shrink-0">
                  <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-olive/20 to-olive/5 flex items-center justify-center group-hover:scale-110 group-hover:rotate-6 transition-all duration-300">
                    <BookOpen className="w-7 h-7 text-olive" />
                  </div>
                </div>
                <div>
                  <h3 className="font-bold text-xl mb-3 text-foreground">Full Amenities</h3>
                  <p className="text-muted-foreground leading-relaxed">Tables, chairs, lighting, and Wi-Fi included. Focus on selling, we handle the rest.</p>
                </div>
              </div>

              <div className="flex gap-5 animate-fade-in group p-6 rounded-2xl hover:bg-card/80 backdrop-blur-sm transition-all duration-500 hover:shadow-xl cursor-pointer hover:-translate-y-1" style={{ animationDelay: "0.3s" }}>
                <div className="flex-shrink-0">
                  <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-primary/20 to-primary/5 flex items-center justify-center group-hover:scale-110 group-hover:rotate-6 transition-all duration-300">
                    <QrCode className="w-7 h-7 text-primary" />
                  </div>
                </div>
                <div>
                  <h3 className="font-bold text-xl mb-3 text-foreground">Contactless Entry</h3>
                  <p className="text-muted-foreground leading-relaxed">Skip the lines with QR code check-in. Fast, secure, and environmentally friendly.</p>
                </div>
              </div>

              <div className="flex gap-5 animate-fade-in group p-6 rounded-2xl hover:bg-card/80 backdrop-blur-sm transition-all duration-300 hover:shadow-lg" style={{ animationDelay: "0.4s" }}>
                <div className="flex-shrink-0">
                  <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-secondary/20 to-secondary/5 flex items-center justify-center group-hover:scale-110 group-hover:rotate-6 transition-all duration-300">
                    <Tag className="w-7 h-7 text-secondary" />
                  </div>
                </div>
                <div>
                  <h3 className="font-bold text-xl mb-3 text-foreground">Genre Discovery</h3>
                  <p className="text-muted-foreground leading-relaxed">Custom badges help visitors find you based on their reading preferences.</p>
                </div>
              </div>

              <div className="flex gap-5 animate-fade-in group p-6 rounded-2xl hover:bg-card/80 backdrop-blur-sm transition-all duration-300 hover:shadow-lg" style={{ animationDelay: "0.5s" }}>
                <div className="flex-shrink-0">
                  <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-olive/20 to-olive/5 flex items-center justify-center group-hover:scale-110 group-hover:rotate-6 transition-all duration-300">
                    <Sparkles className="w-7 h-7 text-olive" />
                  </div>
                </div>
                <div>
                  <h3 className="font-bold text-xl mb-3 text-foreground">Flexible Booking</h3>
                  <p className="text-muted-foreground leading-relaxed">Reserve single or multiple days. Modify or cancel up to 7 days before the event.</p>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Featured FAQs Section */}
        <section className="py-24 lg:py-32 bg-background">
          <div className="container mx-auto px-4">
            <div className="text-center mb-20 animate-fade-in">
              <Badge variant="outline" className="mb-6 px-4 py-2 text-sm font-semibold">FAQ</Badge>
              <h2 className="text-4xl md:text-5xl lg:text-6xl font-bold text-foreground mb-6 tracking-tight">
                Frequently Asked Questions
              </h2>
              <p className="text-lg md:text-xl text-muted-foreground max-w-3xl mx-auto leading-relaxed">
                Everything you need to know about reserving your stall
              </p>
            </div>

            <div className="max-w-4xl mx-auto animate-fade-in">
              <Accordion type="single" collapsible className="w-full space-y-5">
                <AccordionItem value="item-1" className="border-2 rounded-2xl px-8 bg-card/50 backdrop-blur-sm hover:border-primary/30 transition-all duration-300">
                  <AccordionTrigger className="text-left hover:no-underline py-6">
                    <span className="font-bold text-lg">How much does it cost to reserve a stall?</span>
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground text-base pb-6 leading-relaxed">
                    Stall prices vary by location and size. Standard stalls start at LKR 5,000, corner stalls at LKR 7,500, 
                    and premium locations at LKR 10,000. All prices are per day.
                  </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-2" className="border-2 rounded-2xl px-8 bg-card/50 backdrop-blur-sm hover:border-primary/30 transition-all duration-300">
                  <AccordionTrigger className="text-left hover:no-underline py-6">
                    <span className="font-bold text-lg">Can I cancel or modify my booking?</span>
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground text-base pb-6 leading-relaxed">
                    Yes, you can cancel or modify your booking up to 7 days before the event date. 
                    Cancellations made within 7 days are subject to a 25% cancellation fee.
                  </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-3" className="border-2 rounded-2xl px-8 bg-card/50 backdrop-blur-sm hover:border-primary/30 transition-all duration-300">
                  <AccordionTrigger className="text-left hover:no-underline py-6">
                    <span className="font-bold text-lg">What's included with my stall reservation?</span>
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground text-base pb-6 leading-relaxed">
                    Each stall includes a table, two chairs, basic lighting, and Wi-Fi access. 
                    You'll also receive a personalized genre badge and a QR code for easy event entry.
                  </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-4" className="border-2 rounded-2xl px-8 bg-card/50 backdrop-blur-sm hover:border-primary/30 transition-all duration-300">
                  <AccordionTrigger className="text-left hover:no-underline py-6">
                    <span className="font-bold text-lg">How do I access the event with my QR code?</span>
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground text-base pb-6 leading-relaxed">
                    Simply present your QR code (available in your email or booking dashboard) at the vendor entrance. 
                    Our staff will scan it, and you'll be directed to your stall location.
                  </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-5" className="border-2 rounded-2xl px-8 bg-card/50 backdrop-blur-sm hover:border-primary/30 transition-all duration-300">
                  <AccordionTrigger className="text-left hover:no-underline py-6">
                    <span className="font-bold text-lg">Can I reserve multiple stalls?</span>
                  </AccordionTrigger>
                  <AccordionContent className="text-muted-foreground text-base pb-6 leading-relaxed">
                    Absolutely! You can select on;y two stalls during the booking process. 
                    Bulk bookings is not allowed.
                  </AccordionContent>
                </AccordionItem>
              </Accordion>
            </div>
          </div>
        </section>

        {/* CTA Section */}
        <section className="py-28 lg:py-36 gradient-warm relative overflow-hidden">
          <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiNmZmZmZmYiIGZpbGwtb3BhY2l0eT0iMC4xIj48cGF0aCBkPSJNMzYgMzRjMC0yLjIxLTEuNzktNC00LTRzLTQgMS43OS00IDQgMS43OSA0IDQgNCA0LTEuNzkgNC00em0wLTI0YzAtMi4yMS0xLjc5LTQtNC00cy00IDEuNzktNCA0IDEuNzkgNCA0IDQgNC0xLjc5IDQtNHoiLz48L2c+PC9nPjwvc3ZnPg==')] opacity-20"></div>
          
          {/* Animated gradient orbs */}
          <div className="absolute top-10 left-10 w-72 h-72 bg-white/10 rounded-full filter blur-3xl animate-pulse"></div>
          <div className="absolute bottom-10 right-10 w-96 h-96 bg-white/10 rounded-full filter blur-3xl animate-pulse" style={{ animationDelay: '1s' }}></div>
          
          <div className="container mx-auto px-4 relative z-10">
            <div className="max-w-4xl mx-auto text-center animate-fade-in">
              <h2 className="text-4xl md:text-5xl lg:text-6xl font-bold text-white mb-8 tracking-tight leading-tight">
                Ready to showcase your books?
              </h2>
              <p className="text-xl md:text-2xl text-white/90 mb-12 leading-relaxed max-w-3xl mx-auto">
                Join hundreds of vendors at Sri Lanka's largest book event. Secure your spot today and connect with passionate readers.
              </p>
              <div className="flex flex-col sm:flex-row gap-5 justify-center">
                <Button 
                  size="lg" 
                  variant="secondary" 
                  className="text-base sm:text-lg px-10 h-14 shadow-2xl hover-lift hover:shadow-[0_20px_60px_rgba(0,0,0,0.3)] transition-all duration-300 group" 
                  onClick={() => navigate("/reserve")}
                >
                  Reserve Your Stall Now
                  <ArrowRight className="ml-2 w-5 h-5 group-hover:translate-x-1 transition-transform" />
                </Button>
                <Button 
                  size="lg" 
                  variant="outline" 
                  className="text-base sm:text-lg px-10 h-14 bg-white/10 backdrop-blur-sm border-white/30 text-white hover:bg-white/20 hover:border-white/50 transition-all duration-300" 
                  onClick={() => navigate("/auth")}
                >
                  View My Bookings
                </Button>
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
