import api from "./api";

export async function getCredentials({
  search = "",
  category = "ALL",
  sort = "recent",
  page = 1,
  size = 6,
} = {}) {
  const params = {
    page: page - 1,
    size,
  };

  if (category && category !== "ALL") {
    params.category = category;
  }

  if (search.trim()) {
    params.title = search.trim();
  }

  if (sort === "title-asc") {
    params.sortBy = "title";
    params.direction = "asc";
  } else if (sort === "title-desc") {
    params.sortBy = "title";
    params.direction = "desc";
  } else {
    params.sortBy = "createdAt";
    params.direction = "desc";
  }

  const response = await api.get("/vault", { params });

  return response.data.data;
}

export async function getCredentialById(id) {
  const response = await api.get(`/vault/${id}`);
  return response.data.data;
}

export async function createCredential(payload) {
  const response = await api.post("/vault", payload);
  return response.data.data;
}

export async function updateCredential(id, payload) {
  const response = await api.put(`/vault/${id}`, payload);
  return response.data.data;
}

export async function deleteCredential(id) {
  return api.delete(`/vault/${id}`);
}

export async function restoreCredential(id) {
  return api.put(`/vault/${id}/restore`);
}

export async function permanentlyDeleteCredential(id) {
  return api.delete(`/vault/${id}/permanent`);
}

export async function searchCredentials(keyword) {
  const response = await api.get("/vault/search", {
    params: { keyword },
  });

  return response.data.data;
}

export async function getTrash() {
  const response = await api.get("/vault/trash");
  return response.data.data;
}