import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { useAuth } from '../contexts/AuthContext';
import { useToast } from '../contexts/ToastContext';
import { authService } from "../services/authService";

const Auth = () => {
  const [formData, setFormData] = useState({
    businessName: "",
    contactPerson: "",
    email: "",
    phone: "",
    password: "",
    confirmPassword: "",
  });

  const [isLogin, setIsLogin] = useState(true);
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const toast = useToast();
  const navigate = useNavigate();

  // Theme
  const [isDark, setIsDark] = useState<boolean>(() => {
    if (typeof window === "undefined") return false;
    const stored = localStorage.getItem("theme");
    if (stored) return stored === "dark";
    return window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches;
  });

  useEffect(() => {
    if (isDark) document.documentElement.classList.add("dark");
    else document.documentElement.classList.remove("dark");
    localStorage.setItem("theme", isDark ? "dark" : "light");
  }, [isDark]);

  // Login submit (unchanged)
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.email || !formData.password) {
      toast?.error?.('Please fill in all fields');
      return;
    }

    setLoading(true);
    try {
      await login(formData.email, formData.password);

      if (toast?.success) toast.success('Logged in successfully!');

      const role = localStorage.getItem('role');
      if (role === 'ROLE_ORGANIZER') navigate('/organizer');
      else if (role === 'ROLE_PUBLISHER') navigate('/');
      else navigate('/');

    } catch (err: any) {
      if (toast?.error) toast.error(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  // Registration submit
 const handleRegister = async (e: React.FormEvent) => {
  e.preventDefault();

  if (!formData.businessName || !formData.contactPerson || !formData.email || !formData.phone || !formData.password || !formData.confirmPassword) {
    toast?.error?.('Please fill in all fields');
    return;
  }

  if (formData.password !== formData.confirmPassword) {
    toast?.error?.('Passwords do not match');
    return;
  }

  setLoading(true);
  try {
    const response = await authService.register({
      businessName: formData.businessName,
      contactPerson: formData.contactPerson,
      email: formData.email,
      phone: formData.phone,
      password: formData.password,
    });

    // If backend sends error inside 200 response
    if (response.error) {
      toast?.error?.(response.error); 
    } else if (response.message) {
      toast?.success?.(response.message); 
      setIsLogin(true);
    }

  } catch (err: any) {
    // Axios error: get backend response if it exists
    const backendMessage = err.response?.data?.error || err.response?.data?.message;
    if (backendMessage) {
      toast?.error?.(backendMessage); // show backend error message
    } else {
      toast?.error?.('Something went wrong'); // fallback
    }
  } finally {
    setLoading(false);
  }
};



  return (
    <div className="min-h-screen bg-background flex flex-col relative">
      <div className="fixed top-4 right-4 z-50">
        <Button
          onClick={() => setIsDark((s) => !s)}
          aria-label="Toggle theme"
          className="h-10 px-3"
          variant="outline"
        >
          {isDark ? "Light Mode" : "Dark Mode"}
        </Button>
      </div>

      <Header />
      <main className="flex-1 container mx-auto px-4 py-16 flex items-center justify-center">
        <Card className="w-full max-w-md">
          <CardHeader>
            <CardTitle className="text-2xl">{isLogin ? "Login" : "Register"}</CardTitle>
            <CardDescription>
              {isLogin
                ? "Enter your credentials to access your account"
                : "Create an account to reserve your stall"}
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={isLogin ? handleSubmit : handleRegister} className="space-y-4">
              {!isLogin && (
                <>
                  <div className="space-y-2">
                    <Label htmlFor="businessName">Business Name *</Label>
                    <Input
                      id="businessName"
                      required
                      value={formData.businessName}
                      onChange={(e) => setFormData({ ...formData, businessName: e.target.value })}
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="contactPerson">Contact Person *</Label>
                    <Input
                      id="contactPerson"
                      required
                      value={formData.contactPerson}
                      onChange={(e) => setFormData({ ...formData, contactPerson: e.target.value })}
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="phone">Phone *</Label>
                    <Input
                      id="phone"
                      type="tel"
                      required
                      value={formData.phone}
                      onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                    />
                  </div>
                </>
              )}

              <div className="space-y-2">
                <Label htmlFor="email">Email *</Label>
                <Input
                  id="email"
                  type="email"
                  required
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="password">Password *</Label>
                <Input
                  id="password"
                  type="password"
                  required
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                />
              </div>

              {!isLogin && (
                <div className="space-y-2">
                  <Label htmlFor="confirmPassword">Confirm Password *</Label>
                  <Input
                    id="confirmPassword"
                    type="password"
                    required
                    value={formData.confirmPassword}
                    onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
                  />
                </div>
              )}

              <Button type="submit" className="w-full" disabled={loading}>
                {loading ? (isLogin ? "Logging in..." : "Registering...") : isLogin ? "Login" : "Register"}
              </Button>

              <p className="text-sm text-center text-muted-foreground">
                {isLogin ? "Don't have an account? " : "Already have an account? "}
                <button
                  type="button"
                  onClick={() => setIsLogin(!isLogin)}
                  className="text-primary hover:underline"
                >
                  {isLogin ? "Register" : "Login"}
                </button>
              </p>
            </form>
          </CardContent>
        </Card>
      </main>
      <Footer />
    </div>
  );
};

export default Auth;
