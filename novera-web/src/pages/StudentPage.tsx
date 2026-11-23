import {useState} from 'react';
import {useForm} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import {z} from 'zod';
import {api} from '../api/client';
import {Button} from '../../@/components/ui/button.tsx';
import {Input} from '../../@/components/ui/input.tsx';
import {Label} from '../../@/components/ui/label.tsx';
import {Card, CardContent, CardHeader, CardTitle} from '../../@/components/ui/card.tsx';

const studentSchema = z.object({
    firstName: z.string().min(1, 'Required').max(100),
    lastName: z.string().min(1, 'Required').max(100),
    email: z.email('Invalid email').max(100),
    dateOfBirth: z.string().min(1, 'Required'), // YYYY-MM-DD from input
    gender: z.enum(['MALE', 'FEMALE', 'OTHER']), // Adjust if your enum differs
    phone: z.string().regex(/^\+?[0-9]{7,15}$/, '7-15 digits').optional().or(z.literal('')),
    grade: z.string().min(1, 'Required').max(20),
    admissionDate: z.string().min(1, 'Required'),
});

type StudentForm = z.infer<typeof studentSchema>;

export function StudentPage() {
    const [result, setResult] = useState<string>('');
    const [idInput, setIdInput] = useState<string>('');

    const {register, handleSubmit, reset, formState: {errors}} = useForm<StudentForm>({
        resolver: zodResolver(studentSchema),
    });

    const handleGet = async () => {
        try {
            const res = await api.get(`/students/${idInput}`);
            setResult(JSON.stringify(res.data, null, 2));
        } catch (err: any) {
            setResult(`Error: ${err.response?.data?.message || err.message}`);
        }
    };

    const handleCreate = async (data: StudentForm) => {
        try {
            const res = await api.post('/students', data);
            setResult(`Success! Created Student ID: ${res.data.id}\n${JSON.stringify(res.data, null, 2)}`);
            reset();
        } catch (err: any) {
            setResult(`Error: ${err.response?.data?.message || err.message}`);
        }
    };

    const handleUpdate = async (data: StudentForm) => {
        try {
            const res = await api.put(`/students/${idInput}`, data);
            setResult(`Success! Updated Student ID: ${res.data.id}\n${JSON.stringify(res.data, null, 2)}`);
        } catch (err: any) {
            setResult(`Error: ${err.response?.data?.message || err.message}`);
        }
    };

    const handleDelete = async () => {
        try {
            await api.delete(`/students/${idInput}`);
            setResult(`Success! Student ID ${idInput} deactivated.`);
            setIdInput('');
        } catch (err: any) {
            setResult(`Error: ${err.response?.data?.message || err.message}`);
        }
    };

    const inputClass = "mt-1";
    const errorClass = "text-red-500 text-xs mt-1";

    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold">Student Management</h1>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {/* GET / DELETE / UPDATE ID Input */}
                <Card>
                    <CardHeader><CardTitle>Target Student ID</CardTitle></CardHeader>
                    <CardContent className="space-y-4">
                        <div>
                            <Label>Student ID</Label>
                            <Input value={idInput} onChange={(e) => setIdInput(e.target.value)}
                                   placeholder="e.g., 1"/>
                        </div>
                        <div className="flex gap-2">
                            <Button onClick={handleGet} variant="outline">Get by ID</Button>
                            <Button onClick={handleDelete} variant="destructive">Deactivate (Delete)</Button>
                            6
                        </div>
                    </CardContent>
                </Card>

                {/* CREATE / UPDATE Form */}
                <Card>
                    <CardHeader><CardTitle>Student Details Form</CardTitle></CardHeader>
                    <CardContent>
                        <form onSubmit={handleSubmit(handleCreate)} className="space-y-3">
                            <div className="grid grid-cols-2 gap-3">
                                <div>
                                    <Label>First Name</Label>
                                    <Input className={inputClass} {...register('firstName')} />
                                    {errors.firstName && <p className={errorClass}>{errors.firstName.message}</p>}
                                </div>
                                <div>
                                    <Label>Last Name</Label>
                                    <Input className={inputClass} {...register('lastName')} />
                                    {errors.lastName && <p className={errorClass}>{errors.lastName.message}</p>}
                                </div>
                            </div>

                            <div>
                                <Label>Email</Label>
                                <Input className={inputClass} type="email" {...register('email')} />
                                {errors.email && <p className={errorClass}>{errors.email.message}</p>}
                            </div>

                            <div className="grid grid-cols-2 gap-3">
                                <div>
                                    <Label>Date of Birth</Label>
                                    <Input className={inputClass} type="date" {...register('dateOfBirth')} />
                                    {errors.dateOfBirth &&
                                        <p className={errorClass}>{errors.dateOfBirth.message}</p>}
                                </div>
                                <div>
                                    <Label>Admission Date</Label>
                                    <Input className={inputClass} type="date" {...register('admissionDate')} />
                                    {errors.admissionDate &&
                                        <p className={errorClass}>{errors.admissionDate.message}</p>}
                                </div>
                            </div>

                            <div className="grid grid-cols-2 gap-3">
                                <div>
                                    <Label>Gender</Label>
                                    <select
                                        className={`${inputClass} flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm`} {...register('gender')}>
                                        <option value="MALE">MALE</option>
                                        <option value="FEMALE">FEMALE</option>
                                        <option value="OTHER">OTHER</option>
                                    </select>
                                    {errors.gender && <p className={errorClass}>{errors.gender.message}</p>}
                                </div>
                                <div>
                                    <Label>Grade</Label>
                                    <Input className={inputClass} {...register('grade')} placeholder="e.g., 10-A"/>
                                    {errors.grade && <p className={errorClass}>{errors.grade.message}</p>}
                                </div>
                            </div>

                            <div>
                                <Label>Phone (Optional)</Label>
                                <Input className={inputClass} {...register('phone')} placeholder="+1234567890"/>
                                {errors.phone && <p className={errorClass}>{errors.phone.message}</p>}
                            </div>

                            <div className="flex gap-2 pt-2">
                                <Button type="submit" className="flex-1">Create Student</Button>
                                <Button type="button" variant="secondary" className="flex-1"
                                        onClick={handleSubmit(handleUpdate)}>
                                    Update Student (uses ID above)
                                </Button>
                            </div>
                        </form>
                    </CardContent>
                </Card>
            </div>

            {/* Result Display */}
            {result && (
                <Card className="bg-gray-900 text-gray-100">
                    <CardHeader><CardTitle className="text-gray-100">API Response</CardTitle></CardHeader>
                    <CardContent>
                        <pre className="whitespace-pre-wrap text-sm font-mono">{result}</pre>
                    </CardContent>
                </Card>
            )}
        </div>
    );
}