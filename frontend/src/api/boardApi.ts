import api from './axios';
import { BoardRequest, BoardUpdate } from '../types/dto';

// 게시글 목록 조회 (검색 포함)
export const getBoards = (search: string, page: number, size: number) => {
  return api.get('/board/list', {
    params: { search, page, size },
  });
};

// 게시글 상세 조회
export const getBoardById = (boardId: number) => {
  return api.get(`/board/${boardId}`);
};

// 게시글 생성
export const createBoard = (boardData: BoardRequest) => {
  return api.post('/board/create', boardData);
};

// 게시글 수정
export const updateBoard = (boardId: number, boardData: BoardUpdate) => {
  return api.patch(`/board/${boardId}/edit`, boardData);
};

// 게시글 삭제
export const deleteBoard = (boardId: number) => {
  return api.delete(`/board/${boardId}/delete`);
};

// 게시글 좋아요
export const likeBoard = (boardId: number) => {
  return api.post(`/board/${boardId}`);
};
