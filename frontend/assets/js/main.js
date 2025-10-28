// DOM(HTML 문서) 로딩이 완료되면 실행될 함수
document.addEventListener('DOMContentLoaded', () => {
    console.log('Leafy Frontend Initialized!');

    const contentDiv = document.getElementById('content');
    if (contentDiv) {
        contentDiv.innerHTML = '<p>Welcome to Leafy!</p>';
    }

    // 예시: 페이지 로드 시 서버 상태 확인 API 호출
    checkServerStatus();

    // 여기에 이벤트 리스너나 다른 페이지 초기화 로직 추가 예정...
});