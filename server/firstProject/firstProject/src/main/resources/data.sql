INSERT INTO article (id,title,content) VALUES(1,'가가가가','나나나나');
INSERT INTO article (id,title,content) VALUES(2,'가가가가','나나나나');
INSERT INTO article (id,title,content) VALUES(3,'가가가가','나나나나');

-- article 더미 데이터
INSERT INTO article (id,title,content) VALUES(4,'인생 영화는?','댓글 ㄱㄱ');
INSERT INTO article (id,title,content) VALUES(5,'소울 푸드는?','댓글 ㄱㄱ');
INSERT INTO article (id,title,content) VALUES(6,'취미는?','댓글 ㄱㄱ');

-- comment 더미 데이터
---- 4번 게시글의 댓글들
INSERT INTO comment(id, article_id, nickname, body) VALUES(1, 4, 'Park', '굳 윌 헌팅');
INSERT INTO comment(id, article_id, nickname, body) VALUES(2, 4, 'Kim', '아이 엠 샘');
INSERT INTO comment(id, article_id, nickname, body) VALUES(3, 4, 'Choi', '쇼생크의 탈출');
---- 5번 게시글의 댓글들
INSERT INTO comment(id, article_id, nickname, body) VALUES(4, 5, 'Park', '치킨');
INSERT INTO comment(id, article_id, nickname, body) VALUES(5, 5, 'Kim', '샤브샤브');
INSERT INTO comment(id, article_id, nickname, body) VALUES(6, 5, 'Choi', '초밥');
---- 6번 게시글의 댓글들
INSERT INTO comment(id, article_id, nickname, body) VALUES(7, 6, 'Park', '조깅');
INSERT INTO comment(id, article_id, nickname, body) VALUES(8, 6, 'Kim', '유튜브');
INSERT INTO comment(id, article_id, nickname, body) VALUES(9, 6, 'Choi', '독서');