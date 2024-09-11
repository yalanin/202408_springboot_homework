-- users
INSERT INTO `users` (`username`, `email`, `password`, `created_at`, `updated_at`)
VALUES
	('test_update', 'test@example.com', '25d55ad283aa400af464c76d713c07ad', '2024-09-11 14:02:39', '2024-09-11 14:02:39');

-- assets
INSERT INTO `assets` (`user_id`, `name`, `amount`, `created_at`, `updated_at`)
VALUES
	('2', 'test_update', '4321', '2024-09-11 14:03:13', '2024-09-11 14:03:13');
