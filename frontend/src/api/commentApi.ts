import api from './axios';
import { CommentRequest } from '../types/dto';

// 댓글 생성
export const createComment = (boardId: number, commentData: CommentRequest) => {
  return api.post(`/board/${boardId}/comment`, commentData);
};

// 댓글 수정
export const updateComment = (boardId: number, commentId: number, content: string) => {
  return api.patch(`/board/${boardId}/comment/${commentId}`, { content });
};

// 댓글 삭제
export const deleteComment = (boardId: number, commentId: number) => {
  return api.delete(`/board/${boardId}/comment/delete?commentid=${commentId}`);
};
