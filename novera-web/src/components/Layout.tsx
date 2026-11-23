import { Link, Outlet } from 'react-router-dom';

export function Layout() {
    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <nav className="bg-white border-b border-gray-200 p-4">
                <div className="container mx-auto flex items-center gap-6">
                    <Link to="/" className="text-xl font-bold text-blue-700">Novera</Link>
                    <Link to="/students" className="text-gray-600 hover:text-blue-600 font-medium">Students</Link>
                </div>
            </nav>
            <main className="container mx-auto p-6 flex-1">
                <Outlet />
            </main>
        </div>
    );
}