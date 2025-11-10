import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { CreditCard, Loader2 } from "lucide-react";
import { toast } from "@/hooks/use-toast";

interface PaymentStepProps {
  totalPrice: number;
  onPaymentComplete: () => void;
  onBack: () => void;
}

export const PaymentStep = ({ totalPrice, onPaymentComplete, onBack }: PaymentStepProps) => {
  const [processing, setProcessing] = useState(false);

  const handleStripePayment = async () => {
    setProcessing(true);
    
    // Simulate Stripe checkout
    // In production with backend, this would redirect to Stripe Checkout
    await new Promise((resolve) => setTimeout(resolve, 2000));
    
    toast({
      title: "Payment Successful",
      description: "Your payment has been processed successfully.",
    });
    
    setProcessing(false);
    onPaymentComplete();
  };

  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-lg font-semibold mb-2">Payment Details</h3>
        <p className="text-sm text-muted-foreground">
          Complete your payment to confirm your reservation
        </p>
      </div>

      <Card className="border-primary/20">
        <CardContent className="p-6">
          <div className="space-y-4">
            <div className="flex justify-between items-center pb-4 border-b">
              <span className="text-muted-foreground">Total Amount</span>
              <span className="text-2xl font-bold">LKR {totalPrice.toLocaleString()}</span>
            </div>

            <div className="space-y-3">
              <Button
                className="w-full h-12"
                size="lg"
                onClick={handleStripePayment}
                disabled={processing}
              >
                {processing ? (
                  <>
                    <Loader2 className="h-5 w-5 mr-2 animate-spin" />
                    Processing Payment...
                  </>
                ) : (
                  <>
                    <CreditCard className="h-5 w-5 mr-2" />
                    Pay with Stripe
                  </>
                )}
              </Button>

              <p className="text-xs text-center text-muted-foreground">
                Secure payment powered by Stripe
              </p>
            </div>
          </div>
        </CardContent>
      </Card>

      <div className="flex gap-3">
        <Button
          variant="outline"
          className="flex-1"
          onClick={onBack}
          disabled={processing}
        >
          Back
        </Button>
      </div>

      <div className="bg-muted/50 rounded-lg p-4 text-sm">
        <p className="font-medium mb-1">💳 Payment Information</p>
        <ul className="text-muted-foreground space-y-1 list-disc list-inside">
          <li>All major credit/debit cards accepted</li>
          <li>Secure encrypted payment processing</li>
          <li>Instant confirmation upon successful payment</li>
        </ul>
      </div>
    </div>
  );
};