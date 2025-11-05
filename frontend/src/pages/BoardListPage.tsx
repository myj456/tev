import React, { useState, useEffect, useCallback } from "react";
import { Link } from "react-router-dom";
import { getBoards } from "../api/boardApi";
import { BoardList } from "../types/dto";

const BoardListPage = () => {
  const [boards, setBoards] = useState<BoardList[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [page, setPage] = useState(0);
  const [size] = useState(10);

  const fetchBoards = useCallback(async () => {
    setLoading(true);
    try {
      const response = await getBoards(searchKeyword, page, size);
      setBoards(response.data.data || []); // Adjust based on actual API response
    } catch (err) {
      setError("게시물 목록을 불러오는 데 실패했습니다.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [searchKeyword, page, size]);

  useEffect(() => {
    fetchBoards();
  }, [fetchBoards]);

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchKeyword(e.target.value);
    setPage(0);
  };

  return (
    <div className='container mx-auto p-4 lg:p-8'>
      <div className='flex justify-between items-center mb-6'>
        <h1 className='text-3xl font-bold'>게시판</h1>
        <Link
          to='/boards/new'
          className='bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'
        >
          새 글 작성
        </Link>
      </div>
      <div className='mb-4'>
        <input
          type='text'
          placeholder='제목으로 검색...'
          className='p-2 border border-gray-300 rounded-md w-full sm:w-1/3'
          value={searchKeyword}
          onChange={handleSearchChange}
        />
      </div>

      {loading ? (
        <p>게시물을 불러오는 중...</p>
      ) : error ? (
        <p className='text-red-500'>{error}</p>
      ) : boards.length === 0 ? (
        <p>게시물이 없습니다.</p>
      ) : (
        <div className='bg-white shadow-md rounded-lg'>
          <ul className='divide-y divide-gray-200'>
            {boards.map((board) => (
              <li key={board.boardId} className='p-4 hover:bg-gray-50'>
                <Link to={`/boards/${board.boardId}`} className='block'>
                  <div className='flex items-center justify-between'>
                    <p className='text-lg font-semibold text-blue-700 truncate'>
                      {board.title}
                    </p>
                    <div className='text-sm text-gray-500 flex items-center space-x-4'>
                      <span>{board.nickname}</span>
                      <span>
                        {new Date(board.createdAt).toLocaleDateString()}
                      </span>

                      <span>좋아요: {board.likeCount}</span>
                      <span>조회수: {board.viewCount}</span>
                    </div>
                  </div>
                </Link>
              </li>
            ))}
          </ul>
        </div>
      )}
      {/* TODO: 페이지네이션 컨트롤 추가 */}
    </div>
  );
};

export default BoardListPage;
