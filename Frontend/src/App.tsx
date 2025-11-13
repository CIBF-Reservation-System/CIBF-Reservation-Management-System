import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Routes, Route, useLocation, Navigate } from "react-router-dom";

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
import ProtectedRoute from "./components/ProtectedRoute"; // add this

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

const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <TooltipProvider>
        <Toaster />
        <Sonner />
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Index />} />
          <Route path="/auth" element={<Auth />} />

          {/* Protected Routes */}
          <Route
            path="/reserve"
            element={
              <ProtectedRoute>
                <Reserve />
              </ProtectedRoute>
            }
          />
          <Route
            path="/bookings"
            element={
              <ProtectedRoute>
                <Bookings />
              </ProtectedRoute>
            }
          />
          <Route
            path="/genres"
            element={
              <ProtectedRoute>
                <Genres />
              </ProtectedRoute>
            }
          />
          <Route
            path="/organizer"
            element={
              <ProtectedRoute>
                <OrganizerDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/publisher"
            element={
              <ProtectedRoute>
                <PublisherDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/terms"
            element={
              <ProtectedRoute>
                <Terms />
              </ProtectedRoute>
            }
          />
          <Route
            path="/privacy"
            element={
              <ProtectedRoute>
                <Privacy />
              </ProtectedRoute>
            }
          />
          <Route
            path="/refund"
            element={
              <ProtectedRoute>
                <Refund />
              </ProtectedRoute>
            }
          />
          <Route
            path="/confirmation"
            element={
              <ProtectedRoute>
                <ConfirmationRoute />
              </ProtectedRoute>
            }
          />

          {/* Fallback */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </TooltipProvider>
    </QueryClientProvider>
  );
};

export default App;


// import { Toaster } from "@/components/ui/toaster";
// import { Toaster as Sonner } from "@/components/ui/sonner";
// import { TooltipProvider } from "@/components/ui/tooltip";
// import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
// import { Routes, Route, useLocation } from "react-router-dom";
// import Index from "./pages/Index";
// import Auth from "./pages/Auth";
// import Reserve from "./pages/Reserve";
// import Bookings from "./pages/Bookings";
// import Genres from "./pages/Genres";
// import OrganizerDashboard from "./pages/OrganizerDashboard";
// import PublisherDashboard from "./pages/PublisherDashboard";
// import Terms from "./pages/Terms";
// import Privacy from "./pages/Privacy";
// import Refund from "./pages/Refund";
// import { ConfirmationSuccess } from "./components/ConfirmationSuccess";
// import NotFound from "./pages/NotFound";

// const queryClient = new QueryClient();

// const ConfirmationRoute = () => {
//   const location = useLocation();
//   const state = location.state as any;

//   if (!state?.referenceNumber) {
//     return <NotFound />;
//   }

//   return (
//     <ConfirmationSuccess
//       referenceNumber={state.referenceNumber}
//       stallLabels={state.stallLabels}
//       businessName={state.businessName}
//       email={state.email}
//       totalPrice={state.totalPrice}
//     />
//   );
// };

// const App = () => (
//   <QueryClientProvider client={queryClient}>
//     <TooltipProvider>
//       <Toaster />
//       <Sonner />
//       {/* Remove this BrowserRouter */}
//       <Routes>
//         <Route path="/" element={<Index />} />
//         <Route path="/auth" element={<Auth />} />
//         <Route path="/reserve" element={<Reserve />} />
//         <Route path="/bookings" element={<Bookings />} />
//         <Route path="/genres" element={<Genres />} />
//         <Route path="/terms" element={<Terms />} />
//         <Route path="/privacy" element={<Privacy />} />
//         <Route path="/refund" element={<Refund />} />
//         <Route path="/confirmation" element={<ConfirmationRoute />} />
//         <Route path="/organizer" element={<OrganizerDashboard />} />
//         <Route path="/publisher" element={<PublisherDashboard />} />
//         <Route path="*" element={<NotFound />} />
//       </Routes>
//     </TooltipProvider>
//   </QueryClientProvider>
// );

// export default App;
