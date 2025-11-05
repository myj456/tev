import api from './axios';
import { UserBlockCreate } from '../types/dto';

// 사용자 목록 조회
export const getUsers = (search: string, page: number, size: number) => {
  return api.get('/admin/list', { params: { search, page, size } });
};

// 사용자 상세 조회
export const getUserDetails = (userId: number) => {
  return api.get('/admin/user/details', { params: { userid: userId } });
};

// 사용자 차단
export const blockUser = (blockData: UserBlockCreate) => {
  return api.post('/admin/block', blockData);
};

// 사용자 차단 해제
export const unblockUser = (userId: number) => {
  return api.delete(`/admin/block/cancel?userid=${userId}`);
};
