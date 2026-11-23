export function HomePage() {
    const features = [
        {title: 'Student Management', desc: 'Create, view, update, and deactivate student records.'},
        {title: 'Teacher Management', desc: 'Manage teacher profiles and subject assignments. (Coming Soon)'},
        {title: 'Attendance Tracking', desc: 'Daily attendance logs and reports. (Coming Soon)'},
        {title: 'Fee Management', desc: 'Track payments, dues, and generate receipts. (Coming Soon)'},
    ];

    return (
        <div className="space-y-6">
            <h1 className="text-3xl font-bold text-gray-900">Welcome to Novera School Management</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                {features.map((f, i) => (
                    <div key={i} className="p-6 bg-white rounded-lg shadow-sm border border-gray-200">
                        <h2 className="font-semibold text-lg text-gray-800">{f.title}</h2>
                        <p className="text-gray-600 mt-2 text-sm">{f.desc}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}