export const getDateRange = (startDate, endDate) => {
  const start = new Date(startDate);
  const end = new Date(endDate);

  const result = [];

  while (start <= end) {
    result.push(start.toISOString().split("T")[0]);
    start.setDate(start.getDate() + 1);
  }

  return result;
};
