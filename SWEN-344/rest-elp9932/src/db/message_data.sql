INSERT INTO message_list
VALUES 
(DEFAULT, '1', '2', DEFAULT, 'test message A to C', DEFAULT),
(DEFAULT, '1', '2', DEFAULT, 'test message A to C #2', '1873-04-06'),
(DEFAULT, '2', '1', DEFAULT, 'test message C to A', '2142-10-28'),
(DEFAULT, '3', '1', DEFAULT, 'test message M to A', DEFAULT),
(DEFAULT, '3', '4', DEFAULT, 'test message M to L', '1995-02-01'),
(DEFAULT, '4', '3', DEFAULT, 'test message L to M', '1995-02-02'),
(DEFAULT, '3', '4', DEFAULT, 'test message M to L #2', '1995-02-03');

INSERT INTO unread_list
VALUES
(DEFAULT, '2', '1'),
(DEFAULT, '1', '2'),
(DEFAULT, '1', '3'),
(DEFAULT, '3', '4'),
(DEFAULT, '4', '5');