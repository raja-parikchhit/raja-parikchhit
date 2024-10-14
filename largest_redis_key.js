const { createClient } = require('redis');

async function findTopLargestKeys(limit = 10) {
  const redis = createClient();

  redis.on('error', (err) => console.error('Redis Client Error:', err));

  await redis.connect();

  let cursor = '0';
  const keysWithSizes = [];

  try {
    // SCAN loop to iterate through all keys
    do {
      const scanResult = await redis.scan(cursor);
      cursor = scanResult[0];  // Update cursor for the next iteration
      const keys = scanResult[1];

      // Check memory usage for each key
      for (const key of keys) {
        try {
          const size = await redis.memoryUsage(key);
          keysWithSizes.push({ key, size });
        } catch (err) {
          console.error(`Error fetching memory usage for key ${key}:`, err);
        }
      }
    } while (cursor !== '0');  // Repeat until SCAN completes

    // Sort keys by size in descending order and take the top N keys
    const topKeys = keysWithSizes
      .sort((a, b) => b.size - a.size)
      .slice(0, limit);

    console.log(`Top ${limit} Largest Keys:`);
    topKeys.forEach((entry, index) => {
      console.log(`${index + 1}. ${entry.key} - ${entry.size} bytes`);
    });
  } catch (err) {
    console.error('Error scanning keys:', err);
  } finally {
    await redis.disconnect();  // Close the connection
  }
}

// Run the function
findTopLargestKeys(10).catch((err) => {
  console.error('Script Error:', err);
});
