const navbar = {}

function getNavbarByCategory(category, lang = 'en') {
  const items = JSON.parse(JSON.stringify(navbar[category])) // Deep clone
  return filterNavbarItems(items, lang)
}

function filterNavbarItems(items, lang = 'en') {
  return items.filter(v => {
    if (v[`text-${lang}`]) {
      v.text = v[`text-${lang}`]
    } else {
      v.text = v['text-en']
    }
    if (lang != 'en' && v.link && v.link.startsWith('/')) {
      v.link = `/${lang}${v.link}`
    }
    if (v.items && v.items.length > 0) {
      v.items = filterNavbarItems(v.items, lang)
    }
    return v
  })
}

module.exports = {
  getNavbarByCategory
}
