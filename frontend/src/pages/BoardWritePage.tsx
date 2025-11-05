import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { createBoard, getBoardById, updateBoard } from '../api/boardApi';
import { BoardRequest, BoardUpdate } from '../types/dto';

const BoardWritePage = () => {
  const { boardId } = useParams<{ boardId: string }>();
  const navigate = useNavigate();
  const isEditMode = !!boardId;

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchBoardForEdit = useCallback(async () => {
    if (isEditMode) {
      setLoading(true);
      try {
        const response = await getBoardById(parseInt(boardId, 10));
        const board = response.data.data;
        setTitle(board.title);
        setContent(board.content);
      } catch (err) {
        setError('게시물 정보를 불러오는 데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    }
  }, [boardId, isEditMode]);

  useEffect(() => {
    fetchBoardForEdit();
  }, [fetchBoardForEdit]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      if (isEditMode) {
        const boardData: BoardUpdate = { title, content };
        await updateBoard(parseInt(boardId, 10), boardData);
        navigate(`/boards/${boardId}`);
      } else {
        const boardData: BoardRequest = { title, content };
        await createBoard(boardData);
        navigate('/boards');
      }
    } catch (err) {
      setError('게시물 저장에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  if (loading && isEditMode) return <p>게시물을 불러오는 중...</p>;

  return (
    <div className="container mx-auto p-4 lg:p-8">
      <h1 className="text-3xl font-bold mb-6">{isEditMode ? '게시물 수정' : '새 게시물 작성'}</h1>
      <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md">
        <div className="mb-4">
          <label htmlFor="title" className="block text-gray-700 text-sm font-bold mb-2">제목</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>
        <div className="mb-6">
          <label htmlFor="content" className="block text-gray-700 text-sm font-bold mb-2">내용</label>
          <textarea
            id="content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            rows={10}
            required
          ></textarea>
        </div>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        <div className="flex justify-end space-x-4">
          <button type="button" onClick={() => navigate(-1)} className="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded transition duration-300" disabled={loading}>
            취소
          </button>
          <button type="submit" className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-300" disabled={loading}>
            {loading ? '저장 중...' : (isEditMode ? '수정 완료' : '작성 완료')}
          </button>
        </div>
      </form>
    </div>
  );
};

export default BoardWritePage;
