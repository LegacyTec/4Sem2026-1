<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

// Fix Leaflet's default marker icons when bundled with Vite
delete (L.Icon.Default.prototype as any)._getIconUrl
L.Icon.Default.mergeOptions({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
})

const props = defineProps<{
  modelValue: { lat: number; lng: number } | null
  height?: string
}>()
const emit = defineEmits<{
  'update:modelValue': [value: { lat: number; lng: number }]
}>()

let map: L.Map | null = null
let marker: L.Marker | null = null

const MAP_ID = `map-${Math.random().toString(36).slice(2)}`

function setMarker(lat: number, lng: number) {
  if (!map) return
  if (marker) marker.remove()
  marker = L.marker([lat, lng]).addTo(map)
  emit('update:modelValue', { lat, lng })
}

onMounted(() => {
  const el = document.getElementById(MAP_ID)
  if (!el) return

  // Center on Brazil by default
  map = L.map(el, { zoomControl: true }).setView([-15.793889, -47.882778], 4)

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    maxZoom: 19,
  }).addTo(map)

  if (props.modelValue) {
    const { lat, lng } = props.modelValue
    marker = L.marker([lat, lng]).addTo(map)
    map.setView([lat, lng], 14)
  }

  map.on('click', (e: L.LeafletMouseEvent) => {
    setMarker(e.latlng.lat, e.latlng.lng)
  })
})

onUnmounted(() => {
  map?.remove()
  map = null
})

// Geocode using Nominatim (OpenStreetMap, free, no key needed)
async function buscarEndereco(query: string) {
  if (!query.trim() || !map) return
  try {
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}&limit=1`
    const res = await fetch(url, {
      headers: { 'Accept-Language': 'pt-BR,pt', 'User-Agent': 'Altave-SGM/1.0' },
    })
    const data = await res.json()
    if (data.length > 0) {
      const lat = parseFloat(data[0].lat)
      const lng = parseFloat(data[0].lon)
      map.setView([lat, lng], 14)
      setMarker(lat, lng)
    }
  } catch {
    // silently ignore geocode errors
  }
}

defineExpose({ buscarEndereco })
</script>

<template>
  <div class="map-picker-root">
    <div
      :id="MAP_ID"
      class="map-container"
      :style="{ height: height ?? '280px' }"
    />
    <p class="map-hint">
      <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
      </svg>
      Clique no mapa para marcar a localização da planta
      <span v-if="modelValue">
        · <strong>{{ modelValue.lat.toFixed(5) }}, {{ modelValue.lng.toFixed(5) }}</strong>
      </span>
    </p>
  </div>
</template>

<style scoped>
.map-picker-root {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.map-container {
  width: 100%;
  border-radius: var(--radius-md, 8px);
  border: 1px solid var(--gray-300);
  overflow: hidden;
  z-index: 0;
}
.map-hint {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 11px;
  color: var(--gray-500);
  margin: 0;
}
</style>
