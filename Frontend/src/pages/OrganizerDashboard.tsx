import { useState, useEffect } from "react";
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
import { reservationService } from "@/services/reservationService";
import { stallService } from "@/services/stallService";
import { userService } from "@/services/userService";

// Reservation interface
interface Reservation {
  id: string;
  reference?: string;
  businessName: string;
  email: string;
  phone: string;
  stalls: string[];
  date: string;
  status: "confirmed" | "pending" | "cancelled";
  totalAmount?: number;
}

export default function OrganizerDashboard() {
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("all");
  const [areaFilter, setAreaFilter] = useState<string>("all");
  const [stalls, setStalls] = useState<Stall[]>([]);
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [loading, setLoading] = useState(false);
  const [stallsMap, setStallsMap] = useState<Record<string, Stall>>({});

  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/auth");
  };

  // Fetch stalls from backend
  useEffect(() => {
    const fetchStalls = async () => {
      try {
        const data = await stallService.getAllStalls();

        // Map availability from 0/1 to boolean: 1 is available (true), 0 is reserved (false)
        const mappedStalls = data.map((stall: any) => ({
          ...stall,
          available: stall.availability === 1, // true if 1, false if 0 (Reserved)
          id: stall.stallId, // ensure 'id' matches what your code uses
        }));

        setStalls(mappedStalls);
      } catch (err) {
        console.error("Failed to fetch stalls", err);
        toast.error("Failed to fetch stalls");
      }
    };

    fetchStalls();
  }, []);

  // Fetch reservations from backend, relies on stalls state being fetched
  useEffect(() => {
    if (stalls.length === 0) return; // Wait until stalls are fetched

    const fetchReservationsWithDetails = async () => {
      setLoading(true);
      try {
        const reservationsData = await reservationService.getAllReservations();

        // Create initial stall map from the stalls state
        const stallsMapTemp: Record<string, Stall> = {};

        // Use the already fetched stall data to build the map
        stalls.forEach(stall => {
          stallsMapTemp[stall.id] = stall;
        });

        const reservationsWithDetails = await Promise.all(
          reservationsData.map(async (res: any) => {
            let stallId = res.stallId;
            let stallLabel = stallsMapTemp[stallId]?.label || "-";

            // If the stall details were not in the initial stalls fetch (unlikely if getAllStalls is complete),
            // you might need this logic, but ideally you rely on the initial fetch.
            // Since you were calling getStallById, I'll keep the structure but prefer the local map.
            if (!stallsMapTemp[stallId]) {
              try {
                const stallResp = await stallService.getStallById(stallId);
                if (stallResp) {
                  stallLabel = stallResp.label || "-";
                  stallsMapTemp[stallId] = {
                    ...stallResp,
                    available: stallResp.availability === 1,
                    id: stallResp.stallId,
                  };
                }
              } catch (err) {
                console.error("Failed to fetch stall", stallId, err);
              }
            }


            let contactPerson = "-";
            let businessName = res.businessName || "-";
            let email = res.email || "-";
            let phone = res.phoneNumber || "-";

            try {
              const userResp = await userService.getUserById(res.userId);
              contactPerson = userResp.contactPerson || "-";
              businessName = userResp.businessName || businessName;
              email = userResp.email || email;
              phone = userResp.phone || phone;
            } catch (err) {
              console.error("Failed to fetch user", res.userId, err);
            }

            return {
              id: res.reservationId,
              stalls: [stallId], // store stallId
              contactPerson,
              businessName,
              email,
              phone,
              date: new Date(res.reservationDate).toLocaleDateString(),
              status: res.status,
            };
          })
        );

        // Update stallsMap only with the current reservation details for display in the table
        setStallsMap(stallsMapTemp);
        setReservations(reservationsWithDetails);
      } catch (error) {
        console.error(error);
        toast.error("Failed to fetch reservations or stalls");
      } finally {
        setLoading(false);
      }
    };

    fetchReservationsWithDetails();
  }, [stalls]); // Dependency on stalls ensures reservations are fetched after stalls

  // Statistics - Logic is CORRECT, it depends on the 'stalls' state being correctly set
  const totalStalls = stalls.length;
  // Reserved stalls are those where 'available' is false (availability = 0)
  const reservedStalls = stalls.filter((s) => !s.available).length; 
  const availableStalls = totalStalls - reservedStalls;
  const reservedPercentage = totalStalls > 0 ? ((reservedStalls / totalStalls) * 100).toFixed(1) : "0.0";
  const newReservations = reservations.filter((r) => r.status === "pending").length;

  const occupancyData = ["Hall A", "Hall B", "Outdoor"].map((area) => ({
    area,
    reserved: stalls.filter((s) => s.area === area && !s.available).length,
    available: stalls.filter((s) => s.area === area && s.available).length,
  }));

  const pieData = [
    { name: "Reserved", value: reservedStalls, color: "#0E8A8A" },
    { name: "Available", value: availableStalls, color: "#FF8A4B" },
  ];

  const filteredReservations = reservations.filter((reservation) => {
    const matchesSearch =
      reservation.businessName
        .toLowerCase()
        .includes(searchQuery.toLowerCase()) ||
      reservation.email.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus =
      statusFilter === "all" || reservation.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  // Export CSV
  const handleExportCSV = () => {
    const headers = ["Business Name", "Email", "Phone", "Stalls", "Date", "Status"];
    const rows = reservations.map((r) => [
      r.businessName,
      r.email,
      r.phone,
      r.stalls
        .map((stallId) => stallsMap[stallId]?.label || stallId)
        .join("; "),
      r.date,
      r.status,
    ]);
    const csvContent = [headers, ...rows].map((row) => row.join(",")).join("\n");
    const blob = new Blob([csvContent], { type: "text/csv" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `reservations-${new Date().toISOString().split("T")[0]}.csv`;
    link.click();
    URL.revokeObjectURL(url);

    toast.success("CSV exported successfully");
  };

  const handlePrintQR = () => {
    toast.info("QR codes print dialog opened");
  };

  const toggleStallAvailability = (stallId: string) => {
    // Optimistic UI update
    setStalls((prev) =>
      prev.map((stall) =>
        stall.id === stallId ? { ...stall, available: !stall.available } : stall
      )
    );
    // You should also call an API service here to persist this change
    // e.g., stallService.updateStallAvailability(stallId, !stall.available);

    toast.success("Stall status updated");
  };

  const filteredStalls = stalls.filter(
    (stall) => areaFilter === "all" || stall.area === areaFilter
  );

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
              <Printer className="h-4 w-4 mr-2" /> Print QR
            </Button>
            <Button variant="default" onClick={handleExportCSV}>
              <Download className="h-4 w-4 mr-2" /> Export CSV
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
              <CardTitle className="text-sm font-medium">Total Stalls</CardTitle>
              <LayoutGrid className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{totalStalls}</div>
              <p className="text-xs text-muted-foreground mt-1">Across all areas</p>
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
              <p className="text-xs text-muted-foreground mt-1">Ready to reserve</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">New Reservations</CardTitle>
              <TrendingUp className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{newReservations}</div>
              <p className="text-xs text-muted-foreground mt-1">Pending confirmation</p>
            </CardContent>
          </Card>
        </div>

        {/* Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card>
            <CardHeader>
              <CardTitle>Occupancy by Area</CardTitle>
              <CardDescription>Reserved vs Available stalls per area</CardDescription>
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
              <CardDescription>Distribution of stall availability</CardDescription>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={pieData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    // Updated label to show percentage and value only if totalStalls > 0
                    label={({ name, value, percent }) => 
                      totalStalls > 0 
                      ? `${name}: ${value} (${(percent * 100).toFixed(1)}%)` 
                      : `${name}: 0 (0%)`
                    }
                    outerRadius={100}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {pieData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip formatter={(value, name) => [value, name]} />
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
                    <TableHead>Business</TableHead>
                    <TableHead>Email</TableHead>
                    <TableHead>Mobile</TableHead>
                    <TableHead>Stalls</TableHead>
                    <TableHead>Date</TableHead>
                    <TableHead>Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {loading ? (
                    <TableRow>
                      <TableCell colSpan={6} className="text-center">
                        Loading reservations...
                      </TableCell>
                    </TableRow>
                  ) : filteredReservations.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={6} className="text-center">
                        No reservations found
                      </TableCell>
                    </TableRow>
                  ) : (
                    filteredReservations.map((reservation) => (
                      <TableRow key={reservation.id}>
                        <TableCell className="font-medium">
                          {reservation.businessName}
                        </TableCell>
                        <TableCell>
                          <div className="text-sm">
                            <div>{reservation.email}</div>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="text-sm">
                            <div className="text-muted-foreground">
                              {reservation.phone}
                            </div>
                          </div>
                        </TableCell>
                        <TableCell>
                          {reservation.stalls
                            .map((stallId) => stallsMap[stallId]?.label || stallId)
                            .join(", ")}
                        </TableCell>
                        <TableCell>
                          {/* Note: reservation.date is already a formatted string, no need to call new Date().toLocaleDateString() again */}
                          {reservation.date} 
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
                      </TableRow>
                    ))
                  )}
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
              Click stalls to toggle between Available / Reserved
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
                <span className="w-4 h-4 block bg-primary/70 rounded-sm border" />
                Available
              </div>
              <div className="flex items-center gap-2">
                <span className="w-4 h-4 block bg-muted rounded-sm border" />
                Reserved
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}