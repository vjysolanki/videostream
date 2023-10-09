-- Insert into the video table
INSERT INTO video (id, content, delisted, impressions, views, VERSION) VALUES
('1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p', 'SampleContent1', FALSE, 10, 5,0),
('2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q', 'SampleContent2', FALSE, 15, 7,0),
('3c4d5e6f-7g8h-9i0j-1k2l-3m4n5o6p7q8r', 'SampleContent3', FALSE, 12, 6,0);

-- Updated INSERT into the metadata table with comma-separated genres
INSERT INTO metadata (ID, VIDEO_ID, TITLE, DIRECTOR, SYNOPSIS, CREW, YEAR_OF_RELEASE, GENRE, RUNNING_TIME, FORMAT, DELISTED, VERSION) VALUES
('4d5e6f7g-8h9i-0j1k-2l3m-4n5o6p7q8r9s', '1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p', 'SampleTitle1', 'SampleDirector1', 'This is a sample synopsis for SampleTitle1', '["Crew1", "Crew2"]', 2021, 'Action,Adventure', 120, 'HD', FALSE, 0),
('5e6f7g8h-9i0j-1k2l-3m4n-5o6p7q8r9s0t', '2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q', 'SampleTitle2', 'SampleDirector2', 'This is a sample synopsis for SampleTitle2', '["Crew3", "Crew4"]', 2020, 'Drama,Romance', 90, '4K', FALSE, 0),
('6f7g8h9i-0j1k-2l3m-4n5o-6p7q8r9s0t1u', '3c4d5e6f-7g8h-9i0j-1k2l-3m4n5o6p7q8r', 'SampleTitle3', 'SampleDirector3', 'This is a sample synopsis for SampleTitle3', '["Crew5", "Crew6"]', 2019, 'Comedy,Action', 100, 'FHD', FALSE, 0);
