const { handleGetAllStudents, handleGetStudentDetail, handleAddStudent, handleStudentStatus, handleUpdateStudent } = require('../students-controller');

const mockRes = () => {
    const res = {};
    res.status = jest.fn().mockReturnValue(res);
    res.json = jest.fn().mockReturnValue(res);
    return res;
};

jest.mock('../students-service', () => ({
    getAllStudents: jest.fn(),
    getStudentDetail: jest.fn(),
    addNewStudent: jest.fn(),
    setStudentStatus: jest.fn(),
    updateStudent: jest.fn(),
}));

const { getAllStudents, getStudentDetail, addNewStudent, setStudentStatus, updateStudent } = require('../students-service');

describe('Students Controller', () => {

    afterEach(() => jest.clearAllMocks());

    test('handleGetAllStudents should return students list', async () => {
        const mockStudents = [{ id: 2, name: 'Herick' }];
        getAllStudents.mockResolvedValue(mockStudents);

        const req = { query: {} };
        const res = mockRes();

        await handleGetAllStudents(req, res, jest.fn());

        expect(res.status).toHaveBeenCalledWith(200);
        expect(res.json).toHaveBeenCalledWith({ students: mockStudents });
    });

    test('handleGetStudentDetail should return student detail', async () => {
        const mockStudent = { id: 2, name: 'Herick' };
        getStudentDetail.mockResolvedValue(mockStudent);

        const req = { params: { id: 2 } };
        const res = mockRes();

        await handleGetStudentDetail(req, res, jest.fn());

        expect(res.status).toHaveBeenCalledWith(200);
        expect(res.json).toHaveBeenCalledWith(mockStudent);
    });

    test('handleAddStudent should return success message', async () => {
        addNewStudent.mockResolvedValue({ message: 'Student added' });

        const req = { body: { name: 'Herick', email: 'herick@test.com' } };
        const res = mockRes();

        await handleAddStudent(req, res, jest.fn());

        expect(res.status).toHaveBeenCalledWith(201);
        expect(res.json).toHaveBeenCalledWith({ message: 'Student added' });
    });

    test('handleStudentStatus should change student status', async () => {
        setStudentStatus.mockResolvedValue({ message: 'Student status changed successfully' });

        const req = { params: { id: 2 }, body: { status: false }, user: { id: 1 } };
        const res = mockRes();

        await handleStudentStatus(req, res, jest.fn());

        expect(res.status).toHaveBeenCalledWith(200);
        expect(res.json).toHaveBeenCalledWith({ message: 'Student status changed successfully' });
    });

    test('handleUpdateStudent should return updated message', async () => {
        updateStudent.mockResolvedValue({ message: 'Student updated' });

        const req = { params: { id: 2 }, body: { name: 'Herick Updated' } };
        const res = mockRes();

        await handleUpdateStudent(req, res, jest.fn());

        expect(res.status).toHaveBeenCalledWith(200);
        expect(res.json).toHaveBeenCalledWith({ message: 'Student updated' });
    });
});