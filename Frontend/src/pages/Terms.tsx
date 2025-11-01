import { ArrowLeft } from "lucide-react";
import { Link } from "react-router-dom";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Button } from "@/components/ui/button";

const Terms = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      
      <main className="flex-1 container mx-auto px-4 py-12">
        <Link to="/">
          <Button variant="ghost" className="mb-6">
            <ArrowLeft className="mr-2 h-4 w-4" />
            Back to Home
          </Button>
        </Link>

        <article className="max-w-4xl mx-auto prose prose-slate dark:prose-invert">
          <h1 className="text-4xl font-bold mb-2">Terms & Conditions</h1>
          <p className="text-muted-foreground mb-8">Last updated: {new Date().toLocaleDateString()}</p>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">1. Acceptance of Terms</h2>
            <p>
              By accessing and using the Colombo International Bookfair stall reservation system, you agree to be bound by these Terms and Conditions. If you do not agree with any part of these terms, please do not use our services.
            </p>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">2. Stall Reservations</h2>
            <p className="mb-4">
              All stall reservations are subject to availability and confirmation. A reservation is only confirmed upon receipt of full payment and issuance of a confirmation reference number.
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li>Reservations must be made through the official platform</li>
              <li>Each stall has specific dimensions and capacity limits</li>
              <li>Stall assignments are final and cannot be changed without prior approval</li>
              <li>Multiple stalls may be reserved by a single entity</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">3. Payment Terms</h2>
            <p className="mb-4">
              Payment for stall reservations must be completed at the time of booking:
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li>All prices are listed in Sri Lankan Rupees (LKR)</li>
              <li>Payment can be made via accepted payment methods on the platform</li>
              <li>Full payment is required to confirm the reservation</li>
              <li>Receipts will be issued electronically upon successful payment</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">4. Cancellation & Refunds</h2>
            <p className="mb-4">
              Please refer to our Refund Policy for detailed information on cancellations and refunds. In general:
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li>Cancellation requests must be submitted in writing</li>
              <li>Refund eligibility depends on the timing of cancellation</li>
              <li>Processing fees may apply to all refunds</li>
              <li>No refunds for cancellations within 7 days of the event</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">5. Exhibitor Responsibilities</h2>
            <p className="mb-4">
              All exhibitors must:
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li>Comply with all venue rules and regulations</li>
              <li>Maintain their stall in good condition throughout the event</li>
              <li>Not sell or display materials that violate copyright or other laws</li>
              <li>Respect the allocated stall space and not encroach on neighboring stalls</li>
              <li>Present valid QR pass at entry for verification</li>
              <li>Ensure all staff have proper identification</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">6. Event Access</h2>
            <p>
              Access to the event is controlled via QR code passes issued upon confirmed reservation. Lost or stolen QR codes should be reported immediately. The organizer reserves the right to deny entry to anyone without valid credentials.
            </p>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">7. Liability</h2>
            <p className="mb-4">
              Colombo International Bookfair organizers:
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li>Are not liable for any loss, theft, or damage to exhibitor property</li>
              <li>Do not guarantee any specific foot traffic or sales results</li>
              <li>Reserve the right to cancel or modify the event due to unforeseen circumstances</li>
              <li>Are not responsible for any indirect or consequential losses</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">8. Prohibited Activities</h2>
            <p className="mb-4">
              The following activities are strictly prohibited:
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li>Sale of counterfeit or pirated materials</li>
              <li>Display of offensive or inappropriate content</li>
              <li>Excessive noise that disturbs other exhibitors</li>
              <li>Subletting or transferring stall reservations without permission</li>
              <li>Distribution of unauthorized promotional materials outside allocated stall</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">9. Modification of Terms</h2>
            <p>
              We reserve the right to modify these terms at any time. Continued use of the platform after changes constitutes acceptance of the modified terms. Significant changes will be communicated via email to registered users.
            </p>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">10. Contact Information</h2>
            <p>
              For questions or concerns regarding these Terms and Conditions, please contact us at:
            </p>
            <p className="mt-4">
              Email: info@colombobookfair.lk<br />
              Phone: +94 11 234 5678<br />
              Address: BMICH, Colombo 07, Sri Lanka
            </p>
          </section>
        </article>
      </main>

      <Footer />
    </div>
  );
};

export default Terms;