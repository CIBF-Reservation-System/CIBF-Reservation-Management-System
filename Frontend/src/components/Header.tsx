import { Button } from "@/components/ui/button";
import { BookOpen, Menu, X } from "lucide-react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "../contexts/AuthContext";

const Header = () => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, logout } = useAuth();

  const isActive = (path: string) => location.pathname === path;

  const handleLogout = async () => {
    await logout();
    navigate("/auth");
  };

  return (
    <header className="sticky top-0 z-50 w-full border-b border-border bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container mx-auto px-4">
        <div className="flex h-16 items-center justify-between">
          <Link to="/" className="flex items-center gap-2 hover:opacity-80 transition-opacity">
            <BookOpen className="h-7 w-7 text-primary" />
            <span className="text-xl font-bold text-foreground">Colombo Bookfair</span>
          </Link>

          <nav className="hidden md:flex items-center gap-6">
            <Link to="/" className={`text-sm font-medium transition-colors hover:text-primary ${isActive("/") ? "text-primary" : "text-foreground"}`}>Home</Link>
            <Link to="/reserve" className={`text-sm font-medium transition-colors hover:text-primary ${isActive("/reserve") ? "text-primary" : "text-foreground"}`}>Reserve</Link>
            <Link to="/bookings" className={`text-sm font-medium transition-colors hover:text-primary ${isActive("/bookings") ? "text-primary" : "text-foreground"}`}>My Bookings</Link>
          </nav>

          <div className="hidden md:flex items-center gap-3">
            {isAuthenticated ? (
              <Button onClick={handleLogout}>Logout</Button>
            ) : (
              <Link to="/auth">
                <Button>Login / Sign Up</Button>
              </Link>
            )}
          </div>

          <button className="md:hidden p-2" onClick={() => setMobileMenuOpen(!mobileMenuOpen)} aria-label="Toggle menu">
            {mobileMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </button>
        </div>

        {mobileMenuOpen && (
          <div className="md:hidden py-4 animate-slide-in">
            <nav className="flex flex-col gap-4">
              <Link to="/" className={`text-sm font-medium py-2 ${isActive("/") ? "text-primary" : "text-foreground"}`} onClick={() => setMobileMenuOpen(false)}>Home</Link>
              <Link to="/reserve" className={`text-sm font-medium py-2 ${isActive("/reserve") ? "text-primary" : "text-foreground"}`} onClick={() => setMobileMenuOpen(false)}>Reserve</Link>
              <Link to="/bookings" className={`text-sm font-medium py-2 ${isActive("/bookings") ? "text-primary" : "text-foreground"}`} onClick={() => setMobileMenuOpen(false)}>My Bookings</Link>

              <div className="flex flex-col gap-2 pt-4 border-t border-border">
                {isAuthenticated ? (
                  <Button className="w-full" onClick={() => { handleLogout(); setMobileMenuOpen(false); }}>Logout</Button>
                ) : (
                  <Link to="/auth" onClick={() => setMobileMenuOpen(false)}>
                    <Button className="w-full">Login / Sign Up</Button>
                  </Link>
                )}
              </div>
            </nav>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;





// import { Button } from "@/components/ui/button";
// import { BookOpen, Menu, X } from "lucide-react";
// import { Link, useLocation } from "react-router-dom";
// import { useState } from "react";

// const Header = () => {
//   const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
//   const location = useLocation();

//   const isActive = (path: string) => location.pathname === path;

//   return (
//     <header className="sticky top-0 z-50 w-full border-b border-border bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
//       <div className="container mx-auto px-4">
//         <div className="flex h-16 items-center justify-between">
//           <Link to="/" className="flex items-center gap-2 hover:opacity-80 transition-opacity">
//             <BookOpen className="h-7 w-7 text-primary" />
//             <span className="text-xl font-bold text-foreground">Colombo Bookfair</span>
//           </Link>

//           {/* Desktop Navigation */}
//           <nav className="hidden md:flex items-center gap-6">
//             <Link 
//               to="/" 
//               className={`text-sm font-medium transition-colors hover:text-primary ${
//                 isActive('/') ? 'text-primary' : 'text-foreground'
//               }`}
//             >
//               Home
//             </Link>
//             <Link 
//               to="/reserve" 
//               className={`text-sm font-medium transition-colors hover:text-primary ${
//                 isActive('/reserve') ? 'text-primary' : 'text-foreground'
//               }`}
//             >
//               Reserve
//             </Link>
//             <Link 
//               to="/bookings" 
//               className={`text-sm font-medium transition-colors hover:text-primary ${
//                 isActive('/bookings') ? 'text-primary' : 'text-foreground'
//               }`}
//             >
//               My Bookings
//             </Link>
//           </nav>

//           <div className="hidden md:flex items-center gap-3">
//             <Link to="/auth">
//               <Button>Login / Sign Up</Button>
//             </Link>
//           </div>

//           {/* Mobile Menu Button */}
//           <button
//             className="md:hidden p-2"
//             onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
//             aria-label="Toggle menu"
//           >
//             {mobileMenuOpen ? (
//               <X className="h-6 w-6" />
//             ) : (
//               <Menu className="h-6 w-6" />
//             )}
//           </button>
//         </div>

//         {/* Mobile Menu */}
//         {mobileMenuOpen && (
//           <div className="md:hidden py-4 animate-slide-in">
//             <nav className="flex flex-col gap-4">
//               <Link 
//                 to="/" 
//                 className={`text-sm font-medium py-2 ${
//                   isActive('/') ? 'text-primary' : 'text-foreground'
//                 }`}
//                 onClick={() => setMobileMenuOpen(false)}
//               >
//                 Home
//               </Link>
//               <Link 
//                 to="/reserve" 
//                 className={`text-sm font-medium py-2 ${
//                   isActive('/reserve') ? 'text-primary' : 'text-foreground'
//                 }`}
//                 onClick={() => setMobileMenuOpen(false)}
//               >
//                 Reserve
//               </Link>
//               <Link 
//                 to="/bookings" 
//                 className={`text-sm font-medium py-2 ${
//                   isActive('/bookings') ? 'text-primary' : 'text-foreground'
//                 }`}
//                 onClick={() => setMobileMenuOpen(false)}
//               >
//                 My Bookings
//               </Link>
//               <div className="flex flex-col gap-2 pt-4 border-t border-border">
//                 <Link to="/auth" onClick={() => setMobileMenuOpen(false)}>
//                   <Button className="w-full">Login / Sign Up</Button>
//                 </Link>
//               </div>
//             </nav>
//           </div>
//         )}
//       </div>
//     </header>
//   );
// };

// export default Header;
