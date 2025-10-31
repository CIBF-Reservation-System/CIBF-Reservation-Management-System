import { useEffect, useRef, useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Download, Smartphone } from "lucide-react";
import QRCode from "qrcode";

interface QRPassCardProps {
  referenceNumber: string;
  stallLabels: string[];
  eventDate: string;
  onDownload?: () => void;
}

export const QRPassCard = ({ referenceNumber, stallLabels, eventDate, onDownload }: QRPassCardProps) => {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [qrDataUrl, setQrDataUrl] = useState<string>("");

  useEffect(() => {
    const generateQR = async () => {
      if (canvasRef.current) {
        try {
          await QRCode.toCanvas(canvasRef.current, referenceNumber, {
            width: 256,
            margin: 2,
            color: {
              dark: "#263238",
              light: "#FFFFFF",
            },
          });
          
          // Also generate data URL for download
          const dataUrl = await QRCode.toDataURL(referenceNumber, {
            width: 512,
            margin: 2,
          });
          setQrDataUrl(dataUrl);
        } catch (error) {
          console.error("Error generating QR code:", error);
        }
      }
    };

    generateQR();
  }, [referenceNumber]);

  const handleDownload = () => {
    if (qrDataUrl) {
      const link = document.createElement("a");
      link.href = qrDataUrl;
      link.download = `bookfair-pass-${referenceNumber}.png`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      onDownload?.();
    }
  };

  const handleSaveToPhone = () => {
    if (qrDataUrl) {
      // Create a blob and share API for mobile
      fetch(qrDataUrl)
        .then(res => res.blob())
        .then(blob => {
          const file = new File([blob], `bookfair-pass-${referenceNumber}.png`, { type: "image/png" });
          
          if (navigator.share && navigator.canShare({ files: [file] })) {
            navigator.share({
              title: "Colombo Bookfair QR Pass",
              text: `Your booking reference: ${referenceNumber}`,
              files: [file],
            }).catch(err => {
              console.error("Error sharing:", err);
              handleDownload(); // Fallback to download
            });
          } else {
            handleDownload(); // Fallback to download
          }
        });
    }
  };

  return (
    <Card className="overflow-hidden">
      <CardContent className="p-6">
        <div className="space-y-4">
          <div className="text-center">
            <h3 className="font-semibold text-lg mb-2">Your QR Pass</h3>
            <p className="text-sm text-muted-foreground">
              Show this QR at the entrance on {eventDate}
            </p>
          </div>

          <div className="bg-card-elevated p-6 rounded-lg border flex justify-center">
            <canvas ref={canvasRef} className="rounded" />
          </div>

          <div className="text-center space-y-2">
            <p className="text-sm font-medium">Reference: {referenceNumber}</p>
            <p className="text-xs text-muted-foreground">
              Stalls: {stallLabels.join(", ")}
            </p>
          </div>

          <div className="grid grid-cols-2 gap-2">
            <Button
              variant="outline"
              className="w-full"
              onClick={handleDownload}
            >
              <Download className="h-4 w-4 mr-2" />
              Download
            </Button>
            <Button
              variant="secondary"
              className="w-full"
              onClick={handleSaveToPhone}
            >
              <Smartphone className="h-4 w-4 mr-2" />
              Save to Phone
            </Button>
          </div>

          <p className="text-xs text-center text-muted-foreground">
            Valid for admission on {eventDate}
          </p>
        </div>
      </CardContent>
    </Card>
  );
};
