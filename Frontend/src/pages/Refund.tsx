
import { ArrowLeft } from "lucide-react";
import { Link } from "react-router-dom";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { Button } from "@/components/ui/button";

const Refund = () => {
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
          <h1 className="text-4xl font-bold mb-2">Refund Policy</h1>
          <p className="text-muted-foreground mb-8">Last updated: {new Date().toLocaleDateString()}</p>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">1. Overview</h2>
            <p>
              This Refund Policy outlines the terms and conditions under which refunds will be processed for stall reservations at the Colombo International Bookfair. We strive to be fair and transparent in all our refund decisions.
            </p>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">2. Cancellation Timeline</h2>
            <p className="mb-4">
              Refund eligibility depends on when you submit your cancellation request:
            </p>
            
            <div className="bg-muted p-6 rounded-lg mb-4">
              <h3 className="text-lg font-semibold mb-3">More than 30 days before the event</h3>
              <ul className="list-disc pl-6 space-y-2">
                <li>Full refund minus 10% administrative fee</li>
                <li>Refund processed within 10-15 business days</li>
              </ul>
            </div>

            <div className="bg-muted p-6 rounded-lg mb-4">
              <h3 className="text-lg font-semibold mb-3">15-30 days before the event</h3>
              <ul className="list-disc pl-6 space-y-2">
                <li>50% refund of the total amount paid</li>
                <li>Refund processed within 10-15 business days</li>
              </ul>
            </div>

            <div className="bg-muted p-6 rounded-lg mb-4">
              <h3 className="text-lg font-semibold mb-3">8-14 days before the event</h3>
              <ul className="list-disc pl-6 space-y-2">
                <li>25% refund of the total amount paid</li>
                <li>Refund processed within 10-15 business days</li>
              </ul>
            </div>

            <div className="bg-destructive/10 border border-destructive/20 p-6 rounded-lg">
              <h3 className="text-lg font-semibold mb-3 text-destructive">Less than 7 days before the event</h3>
              <ul className="list-disc pl-6 space-y-2">
                <li>No refund available</li>
                <li>Reservation is non-cancellable at this point</li>
              </ul>
            </div>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">3. How to Request a Refund</h2>
            <p className="mb-4">To request a refund, please follow these steps:</p>
            <ol className="list-decimal pl-6 space-y-3">
              <li>
                <strong>Submit a written request:</strong> Send an email to info@colombobookfair.lk with your reservation reference number
              </li>
              <li>
                <strong>Include required information:</strong>
                <ul className="list-disc pl-6 mt-2 space-y-1">
                  <li>Full name and business name</li>
                  <li>Reservation reference number</li>
                  <li>Reason for cancellation</li>
                  <li>Bank account details for refund (if different from original payment method)</li>
                </ul>
              </li>
              <li>
                <strong>Await confirmation:</strong> You will receive an acknowledgment within 2-3 business days
              </li>
              <li>
                <strong>Refund processing:</strong> Approved refunds will be processed according to the timeline specified
              </li>
            </ol>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">4. Refund Method</h2>
            <p className="mb-4">
              Refunds will be issued using the same payment method used for the original transaction. In cases where this is not possible:
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li>Bank transfers to your specified account</li>
              <li>Processing time may vary based on your bank (typically 5-10 business days after refund is initiated)</li>
              <li>You will receive a confirmation email once the refund has been processed</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">5. Event Cancellation by Organizers</h2>
            <p className="mb-4">
              If the Colombo International Bookfair is cancelled or postponed by the organizers:
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li><strong>Full Cancellation:</strong> 100% refund of all paid amounts, no administrative fees</li>
              <li><strong>Postponement:</strong> Option to transfer reservation to the new date or receive a full refund</li>
              <li><strong>Partial Cancellation:</strong> Pro-rated refund based on affected days</li>
            </ul>
            <p className="mt-4">
              Organizers will notify all affected exhibitors via email within 48 hours of the decision.
            </p>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">6. Force Majeure</h2>
            <p>
              In the event of force majeure circumstances (natural disasters, pandemics, government restrictions, etc.) that prevent the event from taking place, the organizers will work with exhibitors to provide fair solutions, which may include:
            </p>
            <ul className="list-disc pl-6 space-y-2 mt-4">
              <li>Credit vouchers for future events</li>
              <li>Partial refunds after deducting incurred costs</li>
              <li>Rescheduling options with no additional fees</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">7. Non-Refundable Items</h2>
            <p className="mb-4">
              The following items are non-refundable under any circumstances:
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li>Additional services purchased separately (advertising, special equipment)</li>
              <li>Processing fees for international transactions</li>
              <li>Costs incurred for customization or special requests</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">8. Stall Transfer Policy</h2>
            <p className="mb-4">
              As an alternative to cancellation, you may transfer your stall reservation to another party:
            </p>
            <ul className="list-disc pl-6 space-y-2">
              <li>Transfer requests must be submitted at least 14 days before the event</li>
              <li>A transfer fee of LKR 5,000 will be charged</li>
              <li>The new party must meet all exhibitor requirements</li>
              <li>Written approval from organizers is required</li>
            </ul>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">9. Disputed Charges</h2>
            <p>
              If you believe you have been incorrectly charged or wish to dispute a transaction, please contact us immediately at info@colombobookfair.lk. We will investigate all disputes promptly and fairly.
            </p>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">10. Changes to Refund Policy</h2>
            <p>
              We reserve the right to modify this Refund Policy at any time. Changes will be effective immediately upon posting to the website. Your continued use of the reservation system after changes constitutes acceptance of the modified policy.
            </p>
          </section>

          <section className="mb-8">
            <h2 className="text-2xl font-semibold mb-4">11. Contact Information</h2>
            <p>
              For refund requests, questions, or concerns, please contact us:
            </p>
            <p className="mt-4">
              Email: info@colombobookfair.lk<br />
              Phone: +94 11 234 5678<br />
              Address: BMICH, Colombo 07, Sri Lanka<br />
              Business Hours: Monday - Friday, 9:00 AM - 5:00 PM (Sri Lanka Time)
            </p>
          </section>
        </article>
      </main>

      <Footer />
    </div>
  );
};

export default Refund;
