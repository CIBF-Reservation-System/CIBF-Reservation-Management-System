import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Routes, Route, useLocation } from "react-router-dom";
import Index from "./pages/Index";
import Auth from "./pages/Auth";
import Reserve from "./pages/Reserve";
import Bookings from "./pages/Bookings";
import Genres from "./pages/Genres";
import OrganizerDashboard from "./pages/OrganizerDashboard";
import PublisherDashboard from "./pages/PublisherDashboard";
import Terms from "./pages/Terms";
import Privacy from "./pages/Privacy";
import Refund from "./pages/Refund";
import { ConfirmationSuccess } from "./components/ConfirmationSuccess";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

const ConfirmationRoute = () => {
  const location = useLocation();
  const state = location.state as any;

  if (!state?.referenceNumber) {
    return <NotFound />;
  }

  return (
    <ConfirmationSuccess
      referenceNumber={state.referenceNumber}
      stallLabels={state.stallLabels}
      businessName={state.businessName}
      email={state.email}
      totalPrice={state.totalPrice}
    />
  );
};

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      {/* Remove this BrowserRouter */}
      <Routes>
        <Route path="/" element={<Index />} />
        <Route path="/auth" element={<Auth />} />
        <Route path="/reserve" element={<Reserve />} />
        <Route path="/bookings" element={<Bookings />} />
        <Route path="/genres" element={<Genres />} />
        <Route path="/terms" element={<Terms />} />
        <Route path="/privacy" element={<Privacy />} />
        <Route path="/refund" element={<Refund />} />
        <Route path="/confirmation" element={<ConfirmationRoute />} />
        <Route path="/organizer" element={<OrganizerDashboard />} />
        <Route path="/publisher" element={<PublisherDashboard />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
