// src/pages/PublisherDashboard.tsx
import React, { useEffect, useState } from "react";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";

const PublisherDashboard = () => {
  const [userEmail, setUserEmail] = useState<string | null>(null);

  useEffect(() => {
    // You can fetch user info from localStorage or API
    const email = localStorage.getItem("email"); // if you stored email
    setUserEmail(email);
  }, []);

  return (
    <div className="min-h-screen flex flex-col bg-background">
      <Header />
      <main className="flex-1 container mx-auto px-4 py-16">
        <Card className="w-full max-w-3xl mx-auto">
          <CardHeader>
            <CardTitle className="text-2xl">Publisher Dashboard</CardTitle>
          </CardHeader>
          <CardContent>
            <p>Welcome, {userEmail || "Publisher"}!</p>
            <p>This is your dashboard where you can manage your content, view stats, and more.</p>
          </CardContent>
        </Card>
      </main>
      <Footer />
    </div>
  );
};

export default PublisherDashboard;
