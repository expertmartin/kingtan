const express = require('express');
const path = require('path');

const app = express();

// Serve static files from the Angular build
app.use(express.static(path.join(__dirname, 'dist/store')));

// Handle SPA routing: serve index.html for all routes
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist/store/index.html'));
});

// Start the server
const port = 80;
app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});