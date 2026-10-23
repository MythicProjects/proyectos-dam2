import { StyleSheet } from "react-native";

export const colors = {
  background: "#f4f1fb",
  primary: "#733dad",
  title: "#2d1b4e",
  text: "#4a4458",
};

export const styles = StyleSheet.create({
  screen: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 24,
    backgroundColor: colors.background,
  },
  list: {
    padding: 24,
    gap: 16,
  },
  card: {
    width: "100%",
    maxWidth: 360,
    gap: 16,
    padding: 24,
    borderRadius: 16,
    backgroundColor: "#ffffff",
    boxShadow: "0 4px 12px rgba(45, 27, 78, 0.12)",
  },
  title: {
    fontSize: 26,
    fontWeight: "700",
    color: colors.title,
  },
  label: {
    fontSize: 16,
    color: colors.text,
  },
  row: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  button: {
    paddingVertical: 14,
    borderRadius: 12,
    alignItems: "center",
    backgroundColor: colors.primary,
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
