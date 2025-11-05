import React, { useState, useEffect, useCallback, useRef } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { getBoardById, deleteBoard, likeBoard } from "../api/boardApi";
import { createComment, updateComment, deleteComment } from "../api/commentApi";
import { Board, CommentRequest } from "../types/dto";
import { useAuth } from "../hooks/useAuth";

const BoardDetailPage = () => {
  const { boardId } = useParams<{ boardId: string }>();
  const navigate = useNavigate();
  const { user, isAuthenticated } = useAuth();
  const [board, setBoard] = useState<Board | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [newComment, setNewComment] = useState("");
  const [editingCommentId, setEditingCommentId] = useState<number | null>(null);
  const [editingContent, setEditingContent] = useState("");
  const isMounted = useRef(false);

  const fetchBoard = useCallback(async () => {
    if (!boardId) return;
    setLoading(true);
    try {
      const response = await getBoardById(parseInt(boardId, 10));
      setBoard(response.data.data);
    } catch (err) {
      setError("게시물 정보를 불러오는 데 실패했습니다.");
    } finally {
      setLoading(false);
    }
  }, [boardId]);

  useEffect(() => {
    if (!isMounted.current) {
      isMounted.current = true;
      fetchBoard();
    }
  }, [fetchBoard]);

  const handleDelete = async () => {
    if (window.confirm("정말로 이 게시물을 삭제하시겠습니까?")) {
      try {
        await deleteBoard(board!.boardId);
        alert("게시물이 삭제되었습니다.");
        navigate("/boards");
      } catch (err) {
        setError("게시물 삭제에 실패했습니다.");
      }
    }
  };

  const handleLike = async () => {
    if (!isAuthenticated) {
      alert("좋아요를 누르려면 로그인이 필요합니다.");
      return;
    }
    if (!board) return;

    const originalBoard = { ...board };
    const newLiked = !board.liked;
    const newLikeCount = newLiked ? board.likeCount + 1 : board.likeCount - 1;

    setBoard({ ...board, liked: newLiked, likeCount: newLikeCount });

    try {
      await likeBoard(board.boardId);
      await fetchBoard();
    } catch (err) {
      setError("좋아요 처리에 실패했습니다.");
      setBoard(originalBoard); // 에러 발생 시 원래 상태로 복구
    }
  };

  const handleCommentSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!isAuthenticated)
      return alert("댓글을 작성하려면 로그인이 필요합니다.");
    const commentData: CommentRequest = { content: newComment };
    try {
      await createComment(board!.boardId, commentData);
      setNewComment("");
      fetchBoard();
    } catch (err) {
      setError("댓글 작성에 실패했습니다.");
    }
  };

  const handleStartEdit = (comment: any) => {
    setEditingCommentId(comment.commentId);
    setEditingContent(comment.content);
  };

  const handleCancelEdit = () => {
    setEditingCommentId(null);
    setEditingContent("");
  };

  const handleUpdateComment = async (commentId: number) => {
    try {
      await updateComment(board!.boardId, commentId, editingContent);
      handleCancelEdit();
      fetchBoard();
    } catch (err) {
      setError("댓글 수정에 실패했습니다.");
    }
  };

  const handleDeleteComment = async (commentId: number) => {
    if (window.confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
      try {
        await deleteComment(board!.boardId, commentId);
        fetchBoard();
      } catch (err) {
        setError("댓글 삭제에 실패했습니다.");
      }
    }
  };

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p className='text-red-500'>{error}</p>;
  if (!board) return <p>게시물을 찾을 수 없습니다.</p>;

  const isAuthor = user?.nickname === board.nickname;

  return (
    <div className='container mx-auto p-4'>
      <div className='bg-white p-6 rounded shadow'>
        <h1 className='text-3xl font-bold mb-2'>{board.title}</h1>
        <div className='text-sm text-gray-500 mb-4'>
          <span>작성자: {board.nickname}</span> |
          <span> 작성일: {new Date(board.createdAt).toLocaleDateString()}</span>{" "}
          |<span> 조회수: {board.viewCount}</span>
        </div>
        <div className='prose max-w-none mb-6'>{board.content}</div>
        <div className='flex items-center space-x-4'>
          <button
            onClick={handleLike}
            className='flex items-center space-x-2 text-gray-600 hover:text-red-500'
          >
            <span>{board.liked ? "❤️" : "🤍"}</span>
            <span>좋아요 {board.likeCount}</span>
          </button>
          {isAuthor && (
            <>
              <Link
                to={`/boards/edit/${board.boardId}`}
                className='text-blue-500 hover:underline'
              >
                수정
              </Link>
              <button
                onClick={handleDelete}
                className='text-red-500 hover:underline'
              >
                삭제
              </button>
            </>
          )}
        </div>
      </div>

      <div className='mt-8'>
        <h2 className='text-2xl font-bold mb-4'>댓글</h2>
        {isAuthenticated && (
          <form onSubmit={handleCommentSubmit} className='mb-6'>
            <textarea
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              className='w-full p-2 border rounded'
              placeholder='댓글을 입력하세요...'
            ></textarea>
            <button
              type='submit'
              className='mt-2 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700'
            >
              댓글 작성
            </button>
          </form>
        )}
        <div>
          {board.commentList.map((comment) => (
            <div
              key={comment.commentId}
              className='bg-gray-100 p-4 rounded mb-4'
            >
              {editingCommentId === comment.commentId ? (
                <div>
                  <textarea
                    value={editingContent}
                    onChange={(e) => setEditingContent(e.target.value)}
                    className='w-full p-2 border rounded'
                  />
                  <div className='mt-2'>
                    <button
                      onClick={() => handleUpdateComment(comment.commentId)}
                      className='px-3 py-1 bg-green-500 text-white rounded mr-2'
                    >
                      저장
                    </button>
                    <button
                      onClick={handleCancelEdit}
                      className='px-3 py-1 bg-gray-400 text-white rounded'
                    >
                      취소
                    </button>
                  </div>
                </div>
              ) : (
                <>
                  <p>{comment.content}</p>
                  <div className='text-sm text-gray-500 mt-2'>
                    작성자: {comment.nickname}
                  </div>
                  {user?.nickname === comment.nickname && (
                    <div className='mt-2'>
                      <button
                        onClick={() => handleStartEdit(comment)}
                        className='text-sm text-blue-500 hover:underline mr-2'
                      >
                        수정
                      </button>
                      <button
                        onClick={() => handleDeleteComment(comment.commentId)}
                        className='text-sm text-red-500 hover:underline'
                      >
                        삭제
                      </button>
                    </div>
                  )}
                </>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default BoardDetailPage;
