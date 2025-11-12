import { useState } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import {
  Download,
  Printer,
  Search,
  LayoutGrid,
  Users,
  TrendingUp,
  MapPin,
} from "lucide-react";
import { toast } from "sonner";
import { Stall, StallSize } from "@/components/StallCard";
import { useAuth } from "@/contexts/AuthContext";
import { useNavigate } from "react-router-dom";

// Mock data for reservations
interface Reservation {
  id: string;
  reference: string;
  businessName: string;
  email: string;
  phone: string;
  stalls: string[];
  date: string;
  status: "confirmed" | "pending" | "cancelled";
  totalAmount: number;
}

const mockReservations: Reservation[] = [
  {
    id: "1",
    reference: "CBF-1234567890-A1B2",
    businessName: "Sarasavi Publishers",
    email: "contact@sarasavi.lk",
    phone: "+94 11 234 5678",
    stalls: ["A1", "A2"],
    date: "2026-02-15",
    status: "confirmed",
    totalAmount: 150000,
  },
  {
    id: "2",
    reference: "CBF-1234567891-C3D4",
    businessName: "Vijitha Yapa",
    email: "info@vijithayapa.com",
    phone: "+94 11 234 5679",
    stalls: ["B5"],
    date: "2026-02-15",
    status: "confirmed",
    totalAmount: 85000,
  },
  {
    id: "3",
    reference: "CBF-1234567892-E5F6",
    businessName: "Godage Publishers",
    email: "contact@godage.lk",
    phone: "+94 11 234 5680",
    stalls: ["C3", "C4", "C5"],
    date: "2026-02-15",
    status: "pending",
    totalAmount: 195000,
  },
];

// Mock stalls data for management grid
const mockStallsForManagement: Stall[] = Array.from({ length: 30 }, (_, i) => {
  const stallNumber = i + 1;
  const label = `${String.fromCharCode(65 + Math.floor(i / 10))}${
    stallNumber % 10 || 10
  }`;
  const sizes: StallSize[] = ["Small", "Medium", "Large"];
  const size = sizes[i % 3];
  const prices = { Small: 50000, Medium: 85000, Large: 120000 };
  const areas = ["Hall A", "Hall B", "Outdoor"];

  return {
    id: `stall-${i + 1}`,
    label,
    size,
    price: prices[size],
    available: Math.random() > 0.3,
    area: areas[Math.floor(i / 10)],
  };
});

export default function OrganizerDashboard() {
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("all");
  const [areaFilter, setAreaFilter] = useState<string>("all");
  const [stalls, setStalls] = useState<Stall[]>(mockStallsForManagement);

  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/auth");
  };

  // Calculate statistics
  const totalStalls = stalls.length;
  const reservedStalls = stalls.filter((s) => !s.available).length;
  const availableStalls = totalStalls - reservedStalls;
  const reservedPercentage = ((reservedStalls / totalStalls) * 100).toFixed(1);
  const newReservations = mockReservations.filter(
    (r) => r.status === "pending"
  ).length;

  // Occupancy by area data
  const occupancyData = [
    {
      area: "Hall A",
      reserved: stalls.filter((s) => s.area === "Hall A" && !s.available)
        .length,
      available: stalls.filter((s) => s.area === "Hall A" && s.available)
        .length,
    },
    {
      area: "Hall B",
      reserved: stalls.filter((s) => s.area === "Hall B" && !s.available)
        .length,
      available: stalls.filter((s) => s.area === "Hall B" && s.available)
        .length,
    },
    {
      area: "Outdoor",
      reserved: stalls.filter((s) => s.area === "Outdoor" && !s.available)
        .length,
      available: stalls.filter((s) => s.area === "Outdoor" && s.available)
        .length,
    },
  ];

  const pieData = [
    { name: "Reserved", value: reservedStalls, color: "#0E8A8A" },
    { name: "Available", value: availableStalls, color: "#FF8A4B" },
  ];

  // Filter reservations
  const filteredReservations = mockReservations.filter((reservation) => {
    const matchesSearch =
      reservation.businessName
        .toLowerCase()
        .includes(searchQuery.toLowerCase()) ||
      reservation.reference.toLowerCase().includes(searchQuery.toLowerCase()) ||
      reservation.email.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus =
      statusFilter === "all" || reservation.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  // Export to CSV
  const handleExportCSV = () => {
    const headers = [
      "Reference",
      "Business Name",
      "Email",
      "Phone",
      "Stalls",
      "Date",
      "Status",
      "Amount",
    ];
    const rows = mockReservations.map((r) => [
      r.reference,
      r.businessName,
      r.email,
      r.phone,
      r.stalls.join("; "),
      r.date,
      r.status,
      r.totalAmount.toString(),
    ]);

    const csvContent = [headers, ...rows]
      .map((row) => row.join(","))
      .join("\n");
    const blob = new Blob([csvContent], { type: "text/csv" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `reservations-${
      new Date().toISOString().split("T")[0]
    }.csv`;
    link.click();
    URL.revokeObjectURL(url);

    toast.success("CSV exported successfully");
  };

  // Print QR codes
  const handlePrintQR = () => {
    toast.info("QR codes print dialog opened");
    // In production, this would trigger a print-optimized view of all QR codes
  };

  // Toggle stall availability
  const toggleStallAvailability = (stallId: string) => {
    setStalls((prev) =>
      prev.map((stall) =>
        stall.id === stallId ? { ...stall, available: !stall.available } : stall
      )
    );
    toast.success("Stall status updated");
  };

  // Filter stalls for management grid
  const filteredStalls = stalls.filter((stall) => {
    return areaFilter === "all" || stall.area === areaFilter;
  });

  return (
    <div className="min-h-screen bg-background">
      <div className="container mx-auto px-4 py-8 space-y-8">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-4xl font-bold text-foreground">
              Organizer Dashboard
            </h1>
            <p className="text-muted-foreground mt-2">
              Manage reservations and stall availability
            </p>
          </div>
          <div className="flex gap-2">
            <Button variant="outline" onClick={handlePrintQR}>
              <Printer className="h-4 w-4 mr-2" />
              Print QR
            </Button>
            <Button variant="default" onClick={handleExportCSV}>
              <Download className="h-4 w-4 mr-2" />
              Export CSV
            </Button>
            <Button variant="destructive" onClick={handleLogout}>
              Logout
            </Button>
          </div>
        </div>

        {/* Statistics Widgets */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                Total Stalls
              </CardTitle>
              <LayoutGrid className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{totalStalls}</div>
              <p className="text-xs text-muted-foreground mt-1">
                Across all areas
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Reserved</CardTitle>
              <Users className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{reservedPercentage}%</div>
              <p className="text-xs text-muted-foreground mt-1">
                {reservedStalls} stalls booked
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Available</CardTitle>
              <MapPin className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{availableStalls}</div>
              <p className="text-xs text-muted-foreground mt-1">
                Ready to reserve
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                New Reservations
              </CardTitle>
              <TrendingUp className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{newReservations}</div>
              <p className="text-xs text-muted-foreground mt-1">
                Pending confirmation
              </p>
            </CardContent>
          </Card>
        </div>

        {/* Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card>
            <CardHeader>
              <CardTitle>Occupancy by Area</CardTitle>
              <CardDescription>
                Reserved vs Available stalls per area
              </CardDescription>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={occupancyData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="area" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="reserved" fill="#0E8A8A" name="Reserved" />
                  <Bar dataKey="available" fill="#FF8A4B" name="Available" />
                </BarChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Overall Occupancy</CardTitle>
              <CardDescription>
                Distribution of stall availability
              </CardDescription>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={pieData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={(entry) => `${entry.name}: ${entry.value}`}
                    outerRadius={100}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {pieData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </div>

        {/* Reservations Table */}
        <Card>
          <CardHeader>
            <CardTitle>Reservations</CardTitle>
            <CardDescription>View and manage all bookings</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="flex flex-col sm:flex-row gap-4 mb-6">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Search by business name, reference, or email..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
              </div>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger className="w-full sm:w-[180px]">
                  <SelectValue placeholder="Filter by status" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Status</SelectItem>
                  <SelectItem value="confirmed">Confirmed</SelectItem>
                  <SelectItem value="pending">Pending</SelectItem>
                  <SelectItem value="cancelled">Cancelled</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="rounded-md border">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Reference</TableHead>
                    <TableHead>Business</TableHead>
                    <TableHead>Contact</TableHead>
                    <TableHead>Stalls</TableHead>
                    <TableHead>Date</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Amount</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredReservations.map((reservation) => (
                    <TableRow key={reservation.id}>
                      <TableCell className="font-mono text-sm">
                        {reservation.reference}
                      </TableCell>
                      <TableCell className="font-medium">
                        {reservation.businessName}
                      </TableCell>
                      <TableCell>
                        <div className="text-sm">
                          <div>{reservation.email}</div>
                          <div className="text-muted-foreground">
                            {reservation.phone}
                          </div>
                        </div>
                      </TableCell>
                      <TableCell>{reservation.stalls.join(", ")}</TableCell>
                      <TableCell>
                        {new Date(reservation.date).toLocaleDateString()}
                      </TableCell>
                      <TableCell>
                        <Badge
                          variant={
                            reservation.status === "confirmed"
                              ? "default"
                              : reservation.status === "pending"
                              ? "secondary"
                              : "destructive"
                          }
                        >
                          {reservation.status}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        LKR {reservation.totalAmount.toLocaleString()}
                      </TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button size="sm" variant="outline">
                            View
                          </Button>
                          <Button size="sm" variant="ghost">
                            Email
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </CardContent>
        </Card>

        {/* Stall Management Grid */}
        <Card>
          <CardHeader>
            <CardTitle>Stall Management</CardTitle>
            <CardDescription>
              Click stalls to toggle between Available / Reserved / Blocked
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="mb-6">
              <Select value={areaFilter} onValueChange={setAreaFilter}>
                <SelectTrigger className="w-full sm:w-[200px]">
                  <SelectValue placeholder="Filter by area" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Areas</SelectItem>
                  <SelectItem value="Hall A">Hall A</SelectItem>
                  <SelectItem value="Hall B">Hall B</SelectItem>
                  <SelectItem value="Outdoor">Outdoor</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-6 lg:grid-cols-8 gap-3">
              {filteredStalls.map((stall) => (
                <button
                  key={stall.id}
                  onClick={() => toggleStallAvailability(stall.id)}
                  className={`p-4 rounded-lg border-2 transition-all hover:scale-105 ${
                    stall.available
                      ? "border-primary bg-primary/10 hover:bg-primary/20"
                      : "border-muted bg-muted hover:bg-muted/80"
                  }`}
                >
                  <div className="text-lg font-bold">{stall.label}</div>
                  <div className="text-xs mt-1">
                    {stall.available ? "Available" : "Reserved"}
                  </div>
                </button>
              ))}
            </div>

            <div className="flex gap-4 mt-6 text-sm">
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded border-2 border-primary bg-primary/10"></div>
                <span>Available</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 rounded border-2 border-muted bg-muted"></div>
                <span>Reserved</span>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
