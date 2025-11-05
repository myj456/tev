import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getUserDetails } from '../api/adminApi';
import { UserDetail } from '../types/dto';

const UserDetailPage = () => {
  const { userId } = useParams<{ userId: string }>();
  const [user, setUser] = useState<UserDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDetails = async () => {
      if (!userId) return;
      setLoading(true);
      try {
        const response = await getUserDetails(parseInt(userId, 10));
        setUser(response.data.data);
      } catch (err) {
        setError('사용자 상세 정보를 불러오는 데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchDetails();
  }, [userId]);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p className="text-red-500">오류: {error}</p>;
  if (!user) return <p>사용자 정보를 찾을 수 없습니다.</p>;

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">{user.nickname}님의 상세 정보</h1>
      <div className="bg-white p-4 rounded shadow mb-4">
        <p><strong>이메일:</strong> {user.email}</p>
        <p><strong>권한:</strong> {user.role}</p>
        <p><strong>가입일:</strong> {new Date(user.createdAt).toLocaleDateString()}</p>
        {user.block?.expiryAt && (
          <p><strong>차단 만료일:</strong> {new Date(user.block.expiryAt).toLocaleString()}</p>
        )}
      </div>

      <div className="mb-4">
        <h2 className="text-xl font-bold mb-2">작성한 게시글 ({user.boardList.length})</h2>
        {user.boardList.length > 0 ? (
          <ul className="list-disc pl-5">
            {user.boardList.map(board => (
              <li key={board.boardId}>
                <Link to={`/boards/${board.boardId}`} className="text-blue-500 hover:underline">
                  {board.title}
                </Link>
              </li>
            ))}
          </ul>
        ) : (
          <p>작성한 게시글이 없습니다.</p>
        )}
      </div>

      <div>
        <h2 className="text-xl font-bold mb-2">작성한 댓글 ({user.commentList.length})</h2>
        {user.commentList.length > 0 ? (
          <ul className="list-disc pl-5">
            {user.commentList.map(comment => (
              <li key={comment.commentId}>
                <Link to={`/boards/${comment.boardId}`} className="text-blue-500 hover:underline">
                  {comment.content}
                </Link>
              </li>
            ))}
          </ul>
        ) : (
          <p>작성한 댓글이 없습니다.</p>
        )}
      </div>
    </div>
  );
};

export default UserDetailPage;
