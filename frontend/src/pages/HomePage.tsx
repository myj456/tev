import React from "react";
import { Link } from "react-router-dom";

const HomePage = () => {
  return (
    <div className='min-h-screen flex flex-col items-center justify-center bg-gray-50'>
      <div className='text-center'>
        <h1 className='text-4xl font-bold text-gray-800 mb-4'>
          Tev 게시판에 오신 것을 환영합니다!
        </h1>
        <p className='text-lg text-gray-600 mb-8'>
          React와 Spring Boot로 구축한 게시판 플랫폼입니다.
        </p>
        <Link
          to='/boards'
          className='bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded-lg text-lg transition duration-300'
        >
          게시판 보기
        </Link>
      </div>
    </div>
  );
};

export default HomePage;
