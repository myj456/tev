import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const Header = () => {
  const { user, logout, isAuthenticated } = useAuth();

  return (
    <header className="bg-white shadow-md">
      <nav className="container mx-auto px-4 lg:px-8 py-4 flex justify-between items-center">
        <Link to="/" className="text-2xl font-bold text-blue-600">Tev 게시판</Link>
        <div className="flex items-center space-x-4">
          <Link to="/boards" className="text-gray-600 hover:text-blue-600">게시판</Link>
          {user?.role === 'ROLE_ADMIN' && (
            <Link to="/admin" className="text-gray-600 hover:text-blue-600">관리자</Link>
          )}
          {isAuthenticated ? (
            <>
              <span className="text-gray-800">환영합니다, {user?.nickname}님</span>
              <button 
                onClick={logout} 
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
              >
                로그아웃
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="text-gray-600 hover:text-blue-600">로그인</Link>
              <Link to="/register" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">회원가입</Link>
            </>
          )}
        </div>
      </nav>
    </header>
  );
};

export default Header;

