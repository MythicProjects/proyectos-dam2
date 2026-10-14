import { StyleSheet } from "react-native";

// Shared styles for both screens
export const styles = StyleSheet.create({
  screen: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 24,
    backgroundColor: "#f4f1fb",
  },
  card: {
    width: "100%",
    maxWidth: 360,
    gap: 20,
    padding: 24,
    borderRadius: 16,
    backgroundColor: "#ffffff",
    boxShadow: "0 4px 12px rgba(45, 27, 78, 0.12)",
  },
  title: {
    fontSize: 26,
    fontWeight: "700",
    color: "#2d1b4e",
    textAlign: "center",
  },
  row: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  label: {
    fontSize: 16,
    color: "#4a4458",
  },
  button: {
    paddingVertical: 14,
    borderRadius: 12,
    alignItems: "center",
    backgroundColor: "#733dad",
  },
  buttonPressed: {
    opacity: 0.8,
  },
  buttonText: {
    fontSize: 16,
    fontWeight: "600",
    color: "#ffffff",
  },
  error: {
    color: "#c0392b",
    textAlign: "center",
  },
});
