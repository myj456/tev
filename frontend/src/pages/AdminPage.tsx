import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { getUsers, blockUser, unblockUser } from '../api/adminApi';
import { UserListResponse } from '../types/dto';
import BlockModal from '../components/BlockModal';

const AdminPage = () => {
  const [users, setUsers] = useState<UserListResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [search, setSearch] = useState('');
  const [page] = useState(0);
  const [size] = useState(20);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);

  const fetchUsers = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await getUsers(search, page, size);
      const fetchedUsers = response.data.data || [];
      
      const now = new Date();
      let usersToUpdate = fetchedUsers;

      // 만료된 사용자 자동 차단 해제
      for (const user of fetchedUsers) {
        if (user.expiryAt && new Date(user.expiryAt) < now) {
          try {
            await unblockUser(user.userId);
            // 로컬 상태를 업데이트하여 즉시 UI에 반영
            usersToUpdate = usersToUpdate.map((u: UserListResponse) => 
              u.userId === user.userId ? { ...u, expiryAt: null } : u
            );
          } catch (err) {
            console.error(`사용자(ID: ${user.userId}) 차단 해제 실패:`, err);
          }
        }
      }
      setUsers(usersToUpdate);

    } catch (err) {
      setError('사용자 목록을 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  }, [search, page, size]);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const openModal = (userId: number) => {
    setSelectedUserId(userId);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setSelectedUserId(null);
    setIsModalOpen(false);
  };

  const handleBlock = async (reason: string, expiryAt: string) => {
    if (selectedUserId) {
      try {
        await blockUser({ userId: selectedUserId, reason, expiryAt });
        alert('사용자가 차단되었습니다.');
        fetchUsers(); // 목록 새로고침
      } catch (err) {
        setError('사용자 차단에 실패했습니다.');
      }
    }
  };

  const handleUnblock = async (userId: number) => {
    if (window.confirm('이 사용자를 차단 해제하시겠습니까?')) {
      try {
        await unblockUser(userId);
        alert('사용자 차단이 해제되었습니다.');
        fetchUsers(); // 목록 새로고침
      } catch (err) {
        setError('사용자 차단 해제에 실패했습니다.');
      }
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">관리자 - 사용자 관리</h1>
      {error && <p className="text-red-500">오류: {error}</p>}
      <div className="mb-4">
        <input
          type="text"
          placeholder="사용자 검색 (이름 또는 이메일)"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="w-full px-3 py-2 border rounded"
        />
      </div>
      {loading ? <p>로딩 중...</p> : (
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="py-2">닉네임</th>
              <th className="py-2">이메일</th>
              <th className="py-2">가입일</th>
              <th className="py-2">권한</th>
              <th className="py-2">차단 만료일</th>
              <th className="py-2">관리</th>
            </tr>
          </thead>
          <tbody>
            {users.map(user => {
              const expiryDate = user.expiryAt ? new Date(user.expiryAt) : null;
              const isBlocked = expiryDate ? expiryDate > new Date() : false;

              return (
                <tr key={user.userId} className="text-center">
                  <td className="py-2">
                    <Link to={`/user/${user.userId}`} className="text-blue-500 hover:underline">
                      {user.nickname}
                    </Link>
                  </td>
                  <td className="py-2">{user.email}</td>
                  <td className="py-2">{new Date(user.createdAt).toLocaleDateString()}</td>
                  <td className="py-2">{user.role}</td>
                  <td className="py-2">{isBlocked && expiryDate ? expiryDate.toLocaleString() : '-' }</td>
                  <td className="py-2">
                    {user.role !== 'ROLE_ADMIN' && (
                      isBlocked ? (
                        <button onClick={() => handleUnblock(user.userId)} className="bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600">해제</button>
                      ) : (
                        <button onClick={() => openModal(user.userId)} className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600">차단</button>
                      )
                    )}
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      )}
      {selectedUserId && (
        <BlockModal
          isOpen={isModalOpen}
          onClose={closeModal}
          onConfirm={handleBlock}
          userId={selectedUserId}
        />
      )}
    </div>
  );
};

export default AdminPage;