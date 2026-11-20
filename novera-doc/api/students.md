# Students API (v0.0.1)

Base path: `/api/v1/students`. All responses use the standard envelope
`{ success, message, data, timestamp }`; errors use
`{ success:false, status, error, message, path, fieldErrors, timestamp }`.

| Method | Path | Notes |
|---|---|---|
| GET | `/` | Query: `q` (name/email), `grade`, `status`, `page` (0+), `size` (1-100, default 20), `sortBy` (`FIRST_NAME`, `LAST_NAME`, `GRADE`, `ADMISSION_DATE`, `CREATED_AT`), `direction` (`ASC`/`DESC`). Returns `{content, page, size, totalElements, totalPages}` |
| GET | `/{id}` | 404 if not found |
| POST | `/` | 201 + `Location` header. `status` optional (defaults to `ACTIVE`) |
| PUT | `/{id}` | Full update; `status` only changes if sent |
| DELETE | `/{id}` | Soft delete: sets status `INACTIVE` |

## Student body
```json
{
  "firstName": "Aarav",
  "lastName": "Sharma",
  "dateOfBirth": "2014-05-12",
  "gender": "MALE",
  "email": "aarav@example.com",
  "phone": "9876543210",
  "grade": "5-A",
  "admissionDate": "2024-06-15",
  "status": "ACTIVE"
}
```
Rules: names required (max 100); `dateOfBirth` must be in the past; `gender` is `MALE|FEMALE|OTHER`;
`email` optional but valid; `phone` optional, 7-15 digits with optional leading `+` (omit it rather than sending `""`).
