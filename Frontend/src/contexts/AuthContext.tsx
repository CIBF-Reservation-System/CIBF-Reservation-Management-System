import React, { createContext, useContext, useState, useEffect, ReactNode } from "react";
import { authService } from "../services/authService";

interface User {
  email: string;
  role: string;
  [key: string]: any;
}

interface AuthContextType {
  isAuthenticated: boolean;
  user: User | null;
  login: (email: string, password: string) => Promise<User>;
  logout: () => Promise<void>;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
}

interface AuthProviderProps {
  children: ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<User | null>(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initAuth = async () => {
      try {
        const userData = await authService.getProfile();
        setUser(userData);
        setIsAuthenticated(true);
      } catch {
        setUser(null);
        setIsAuthenticated(false);
      } finally {
        setLoading(false);
      }
    };
    initAuth();
  }, []);

  const login = async (email: string, password: string) => {
    const response = await authService.login(email, password);
    setUser(response.user);
    setIsAuthenticated(true);
    return response.user;
  };

  const logout = async () => {
    await authService.logout();
    setUser(null);
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
}



// import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
// import { authService } from '../services/authService';

// interface User {
//   email: string;
//   role: string;
//   [key: string]: any;
// }

// interface AuthContextType {
//   isAuthenticated: boolean;
//   user: User | null;
//   login: (email: string, password: string) => Promise<User>;
//   logout: () => Promise<void>;
//   loading: boolean;
// }

// const AuthContext = createContext<AuthContextType | undefined>(undefined);

// export function useAuth() {
//   const context = useContext(AuthContext);
//   if (!context) throw new Error('useAuth must be used within an AuthProvider');
//   return context;
// }

// interface AuthProviderProps {
//   children: ReactNode;
// }

// export function AuthProvider({ children }: AuthProviderProps) {
//   const [user, setUser] = useState<User | null>(null);
//   const [isAuthenticated, setIsAuthenticated] = useState(false);
//   const [loading, setLoading] = useState(true);

//   useEffect(() => {
//     const initAuth = async () => {
//       try {
//         const userData = await authService.getProfile();
//         setUser(userData);
//         setIsAuthenticated(true);
//       } catch {
//         setUser(null);
//         setIsAuthenticated(false);
//       } finally {
//         setLoading(false);
//       }
//     };
//     initAuth();
//   }, []);

//   const login = async (email: string, password: string) => {
//     const response = await authService.login(email, password);
//     setUser(response.user);
//     setIsAuthenticated(true);
//     return response.user;
//   };

//   const logout = async () => {
//     await authService.logout();
//     setUser(null);
//     setIsAuthenticated(false);
//   };

//   return (
//     <AuthContext.Provider value={{ isAuthenticated, user, login, logout, loading }}>
//       {children}
//     </AuthContext.Provider>
//   );
// }
