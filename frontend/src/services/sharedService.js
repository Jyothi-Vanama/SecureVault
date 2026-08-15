import api from "./api";

export async function getReceivedShares() {
  const response = await api.get("/share/received");

  return response.data.data;
}