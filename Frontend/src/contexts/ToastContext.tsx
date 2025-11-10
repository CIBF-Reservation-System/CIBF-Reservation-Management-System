import React, { createContext, useContext, useState, ReactNode } from 'react';
import { Toast, ToastProvider as RadixToastProvider, ToastViewport, ToastClose, ToastTitle, ToastDescription } from '../components/ui/toast';

type ToastType = 'info' | 'success' | 'error' | 'warning';

interface ToastContextType {
  showToast: (message: string, type?: ToastType) => void;
  success: (message: string) => void;
  error: (message: string) => void;
  info: (message: string) => void;
  warning: (message: string) => void;
}

interface ToastItem {
  id: number;
  message: string;
  type: ToastType;
}

interface ToastProviderProps {
  children: ReactNode;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export function useToast() {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
}

// Map your custom toast types to Toast component variants
const toastTypeToVariant = (type: ToastType): 'default' | 'destructive' => {
  switch (type) {
    case 'success':
      return 'default';
    case 'info':
      return 'default';
    case 'warning':
      return 'destructive';
    case 'error':
      return 'destructive';
    default:
      return 'default';
  }
};

export function ToastProvider({ children }: ToastProviderProps) {
  const [toasts, setToasts] = useState<ToastItem[]>([]);

  const showToast = (message: string, type: ToastType = 'info') => {
    const id = Date.now();
    const toast: ToastItem = { id, message, type };

    setToasts(prev => [...prev, toast]);

    setTimeout(() => {
      setToasts(prev => prev.filter(t => t.id !== id));
    }, 5000);
  };

  const removeToast = (id: number) => {
    setToasts(prev => prev.filter(t => t.id !== id));
  };

  const value: ToastContextType = {
    showToast,
    success: (message: string) => showToast(message, 'success'),
    error: (message: string) => showToast(message, 'error'),
    info: (message: string) => showToast(message, 'info'),
    warning: (message: string) => showToast(message, 'warning'),
  };

  return (
    <ToastContext.Provider value={value}>
      <RadixToastProvider>
        {children}
        <ToastViewport className="fixed top-4 right-4 z-50 space-y-2" />
        {toasts.map(toast => (
          <Toast
            key={toast.id}
            variant={toastTypeToVariant(toast.type)}
          >
            <ToastTitle>{toast.type.toUpperCase()}</ToastTitle>
            <ToastDescription>{toast.message}</ToastDescription>
            <ToastClose onClick={() => removeToast(toast.id)} />
          </Toast>
        ))}
      </RadixToastProvider>
    </ToastContext.Provider>
  );
}
