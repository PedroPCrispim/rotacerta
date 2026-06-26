const sanitizeQuery = (value) => {
  if (!value) {
    return ''
  }

  return String(value).trim()
}

export const buildWazeDeepLink = ({ latitude, longitude, query }) => {
  if (Number.isFinite(latitude) && Number.isFinite(longitude)) {
    return `https://waze.com/ul?ll=${latitude},${longitude}&navigate=yes`
  }

  const sanitizedQuery = sanitizeQuery(query)
  if (!sanitizedQuery) {
    return ''
  }

  return `https://waze.com/ul?q=${encodeURIComponent(sanitizedQuery)}&navigate=yes`
}

export const buildGoogleMapsLink = ({ latitude, longitude, query }) => {
  if (Number.isFinite(latitude) && Number.isFinite(longitude)) {
    return `https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}`
  }

  const sanitizedQuery = sanitizeQuery(query)
  if (!sanitizedQuery) {
    return ''
  }

  return `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(sanitizedQuery)}`
}

export const hasExternalNavigationLink = (...links) => links.some((link) => Boolean(link))
